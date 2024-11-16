package com.ulpgc.uniMatch.data.infrastructure.events

import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications

data class EventNotificationEvent(
    val notification: Notifications
) : Event {
    override val type: String = "eventNotification"
}