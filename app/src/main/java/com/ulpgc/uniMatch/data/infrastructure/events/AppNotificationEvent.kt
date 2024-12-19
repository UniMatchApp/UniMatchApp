package com.ulpgc.uniMatch.data.infrastructure.events

import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.domain.models.notification.Notification

data class AppNotificationEvent(
    val notification: Notification
) : Event {
    override val type: String = "appNotification"
}