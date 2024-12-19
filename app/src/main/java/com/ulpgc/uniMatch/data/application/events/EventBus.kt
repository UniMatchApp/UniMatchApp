package com.ulpgc.uniMatch.data.application.events

interface EventBus {
    fun subscribeToEvents(eventListener: EventListener)
    suspend fun emitEvent(event: Event)
}
