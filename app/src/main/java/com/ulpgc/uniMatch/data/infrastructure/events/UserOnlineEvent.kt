package com.ulpgc.uniMatch.data.infrastructure.events

import com.ulpgc.uniMatch.data.application.events.Event

data class UserOnlineEvent(val userId: String) : Event {
    override val type: String = "userOnline"
}