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

open class EventViewModel(
    private val eventService: EventService
) : ViewModel() {


    private val _eventsData = MutableStateFlow<List<Event>?>(null)
    val eventsData: StateFlow<List<Event>?> get() = _eventsData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _eventData = MutableStateFlow<Event?>(null)
    val eventData: StateFlow<Event?> get() = _eventData

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

    fun setTitle(title: String) {
        val event = eventData.value
        if (event != null) {
            _eventData.value = event.copy(title = title)
        }
    }

    fun setLocation(latitude: Double, longitude: Double, altitude: Double) {
        val location = Location(
            latitude = latitude,
            longitude = longitude,
            altitude = altitude
        )
        val event = eventData.value
        if (event != null) {
            _eventData.value = event.copy(location = location)
        }
    }


    fun createEvent() {
        performLoadingAction {
            val event = eventData.value
            Log.i("EventViewModel", "Event: $event")
            if (event != null) {
                val file = File(event.attachment)
                val uri: Uri = Uri.fromFile(file)
                eventService.create(event.title, event.price, event.location, DateParser.formatDateToString(event.date), uri, event.surveys)
            } else {
                throw IllegalStateException("No event data available")
            }
        }
    }


    fun createSurvey(surveys: MutableList<Survey>, title: String, options: List<String>): MutableList<Survey> {
        val survey = Survey(
            title = title,
            options = options.toSet()
        )
        surveys.add(survey)
        return surveys
    }

    fun setDateTime(date: String) {
        val event = eventData.value
        if (event != null) {
            _eventData.value = event.copy(date = DateParser.formatStringToDate(date))
        }
    }

    fun setUri(it: Uri) {
        val event = eventData.value
        if (event != null) {
            _eventData.value = event.copy(attachment = it.toString())
        }
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

