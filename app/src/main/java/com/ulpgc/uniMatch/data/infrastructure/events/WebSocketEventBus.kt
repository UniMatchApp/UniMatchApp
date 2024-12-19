package com.ulpgc.uniMatch.data.infrastructure.events

import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.application.events.EventListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.runBlocking

class WebSocketEventBus : EventBus {

    private val _eventFlow = MutableSharedFlow<Event>(replay = 0)
    private val eventFlow: SharedFlow<Event> = _eventFlow

    override fun subscribeToEvents(eventListener: EventListener) {
        runBlocking {
            eventFlow.collect { event ->
                eventListener.onEventReceived(event)
            }
        }
    }

    override suspend fun emitEvent(event: Event) {
        _eventFlow.emit(event)
    }
}
