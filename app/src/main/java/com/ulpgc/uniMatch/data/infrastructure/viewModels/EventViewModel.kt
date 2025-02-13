package com.ulpgc.uniMatch.data.infrastructure.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.EventService
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Profile
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

    fun loadEvents() {
        performLoadingAction {
            _eventsData.value = eventService.getAll().getOrThrow()
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