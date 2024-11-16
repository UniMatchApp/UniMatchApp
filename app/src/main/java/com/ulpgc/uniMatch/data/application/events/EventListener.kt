package com.ulpgc.uniMatch.data.application.events

interface EventListener {
    suspend fun onEventReceived(event: Event)
}
