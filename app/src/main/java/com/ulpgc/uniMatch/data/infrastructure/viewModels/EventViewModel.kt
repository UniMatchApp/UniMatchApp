package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.EventService
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.ui.screens.utils.DateParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date


open class EventViewModel(
    private val eventService: EventService
) : ViewModel() {


    private val _eventsData = MutableStateFlow<List<Event>?>(null)
    val eventsData: StateFlow<List<Event>?> get() = _eventsData

    private val _eventCreated = MutableStateFlow(EventData())
    val eventCreated: StateFlow<EventData> get() = _eventCreated

    private val _eventData = MutableStateFlow<Event?>(null)
    val eventData: StateFlow<Event?> get() = _eventData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    fun loadEvents() {
        performLoadingAction {
            _eventsData.value = eventService.getAll().getOrThrow()
        }
    }

    fun loadEvent(eventId: String) {
        performLoadingAction {
            _eventData.value = eventService.getOne(eventId).getOrThrow()
        }
    }

    suspend fun createEvent(): Result<Unit> {
        return try {
            val event = _eventCreated.value

            val title = event.title
            val attachment = event.attachment
            val date = event.date
            val uri = event.attachment

            if (title.isNullOrBlank()) return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
            if (uri == null) return Result.failure(IllegalArgumentException("Debe haber un archivo adjunto"))
            if (date == null) return Result.failure(IllegalArgumentException("Debe haber una fecha válida"))

            Log.i("EventViewModel", "Creando evento: $event")

            eventService.create(
                title,
                0.0,
                event.location,
                DateParser.formatDateToString(date),
                uri,
                event.surveys ?: emptyList()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun setTitle(title: String) {
        _eventCreated.value = _eventCreated.value.copy(title = title)
        Log.i("EventViewModel", "Título guardado: ${_eventCreated.value.title}")
    }

    fun setLocation(latitude: Double, longitude: Double, altitude: Double) {
        Log.i("EventViewModel", "Ubicación guardada: $latitude, $longitude, $altitude")
        val location = Location(latitude, longitude, altitude)
        _eventCreated.value = _eventCreated.value.copy(location = location)
    }

    fun setDateTime(date: String) {
        _eventCreated.value = _eventCreated.value.copy(date = DateParser.formatStringToDate(date))
    }

    fun setUri(uri: Uri) {
        _eventCreated.value = _eventCreated.value.copy(attachment = uri)
    }

    fun createSurvey() {
        val survey = Survey(
            title = "",
            options = emptyMap()
        )
        val updatedSurveys = _eventCreated.value.surveys?.toMutableList() ?: mutableListOf()
        updatedSurveys.add(survey)

        _eventCreated.value = _eventCreated.value.copy(surveys = updatedSurveys)
        Log.i("EventViewModel", "Encuesta creada: ${_eventCreated.value.surveys}")
    }

    fun voteSurvey(eventId: String, surveyTitle: String, selectedOption: String) {
        performLoadingAction {
            eventService.selectSurvey(eventId, surveyTitle, selectedOption)
            loadEvent(eventId)
        }
    }

    fun dislikeEvent(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.dislikeEvent(eventId)
                loadEvent(eventId)
            }
        }
    }

    fun likeEvent(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.likeEvent(eventId)
                loadEvent(eventId)
            }
        }
    }

    fun addParticipation(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.participateEvent(eventId)
                loadEvent(eventId)
            }
        }
    }

    fun removeParticipation(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.removeParticipation(eventId)
                loadEvent(eventId)
            }
        }
    }

    fun setSurvey(survey: Survey, title: String, options: List<String>) {
        val updatedSurveys = _eventCreated.value.surveys?.toMutableList() ?: mutableListOf()

        val surveyIndex = updatedSurveys.indexOf(survey)
        if (surveyIndex != -1) {
            val optionsMap: Map<String, Set<String>> = options.associateWith { setOf("") }
            updatedSurveys[surveyIndex] = Survey(
                title = title,
                options = optionsMap
            )

            _eventCreated.value = _eventCreated.value.copy(surveys = updatedSurveys)
            Log.i("EventViewModel", "Encuesta actualizada en el índice $surveyIndex: ${_eventCreated.value.surveys}")
        } else {
            Log.e("EventViewModel", "Encuesta no encontrada en la lista: $survey")
        }
    }

    fun deleteSurvey(survey: Survey) {
        _eventCreated.value = _eventCreated.value.copy(
            surveys = _eventCreated.value.surveys?.filter { it != survey }
        )
        Log.i("DeleteSurvey", "Surveys after deletion: ${_eventCreated.value.surveys}")
    }

    fun deleteEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val result = eventService.delete(eventId)

            result.onFailure {
                Log.e("EventViewModel", "Error al eliminar evento: $it")
                onFailure("Error al eliminar evento")
            }.onSuccess {
                Log.i("EventViewModel", "Evento eliminado exitosamente.")
                onSuccess() // Llamamos al callback en caso de éxito
            }
        }
    }

    private fun performLoadingAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                action()
            } finally {
                _isLoading.value = false
            }
        }
    }



}

data class EventData(
    val title: String? = null,
    val attachment: Uri? = null,
    val location: Location? = null,
    val date: Date? = null,
    val surveys: List<Survey>? = null
)

