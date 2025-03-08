package com.ulpgc.uniMatch.data.infrastructure.events

import com.ulpgc.uniMatch.data.application.events.Event

data class GetUserStatusEvent(val userId: String, val status: String) : Event {
    override val type: String = "getUserStatus"
}