package com.ulpgc.uniMatch.data.infrastructure.events

import com.ulpgc.uniMatch.data.application.events.Event

data class TypingEvent(val userId: String, val targetUserId: String) : Event {
    override val type: String = "typing"
}