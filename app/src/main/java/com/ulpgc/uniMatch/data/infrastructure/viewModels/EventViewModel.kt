package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.EventService
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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


    fun createEvent(
        title: String,
        price: Double,
        longitude: Double,
        latitude: Double,
        date: String,
        attachment: Uri,
        surveys: List<Survey>,
    ) {
        performLoadingAction {
            val location = Location(
                longitude = longitude,
                latitude = latitude,
                altitude = 0.0
            )
            eventService.create(title, price, location, date, attachment, surveys)
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


}

