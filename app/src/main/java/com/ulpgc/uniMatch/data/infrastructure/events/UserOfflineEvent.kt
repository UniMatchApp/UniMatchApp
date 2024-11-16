package com.ulpgc.uniMatch.data.infrastructure.events

import com.ulpgc.uniMatch.data.application.events.Event

data class UserOfflineEvent(val userId: String) : Event {
    override val type: String = "userOffline"
}