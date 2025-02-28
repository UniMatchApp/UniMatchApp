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



    suspend fun createEvent(): Result<Unit> {
        return try {
            val event = _eventCreated.value

            when {
                event.title.isNullOrBlank() -> return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
                event.attachment.isNullOrBlank() -> return Result.failure(IllegalArgumentException("Debe haber un archivo adjunto"))
                event.date == null -> return Result.failure(IllegalArgumentException("Debe haber una fecha válida"))
            }

            Log.i("EventViewModel", "Creando evento: $event")

            val file = File(event.attachment)
            val uri: Uri = Uri.fromFile(file)

            eventService.create(
                event.title!!,
                0.0,
                event.location ?: null,
                DateParser.formatDateToString(event.date!!),
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
        _eventCreated.value = _eventCreated.value.copy(attachment = uri.toString())
    }



    fun createSurvey(surveys: MutableList<Survey>, title: String, options: List<String>): MutableList<Survey> {
        val survey = Survey(
            title = title,
            options = options.toSet()
        )
        surveys.add(survey)
        return surveys
    }

    fun dislikeEvent(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.dislikeEvent(eventId)
            }
        }
    }

    fun likeEvent(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.likeEvent(eventId)
            }
        }
    }

    fun addParticipation(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.participateEvent(eventId)
            }
        }
    }

    fun removeParticipation(eventId: String) {
        performLoadingAction {
            val event = eventData.value
            if (event != null) {
                eventService.removeParticipation(eventId)
            }
        }
    }
}

data class EventData(
    val title: String? = null,
    val attachment: String? = null,
    val location: Location? = null,
    val date: Date? = null,
    val surveys: List<Survey>? = null
)

