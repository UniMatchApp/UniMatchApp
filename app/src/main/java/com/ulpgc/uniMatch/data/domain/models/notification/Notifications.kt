package com.ulpgc.uniMatch.data.domain.models.notification

import NotificationPayload
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.enums.NotificationType
import com.ulpgc.uniMatch.data.infrastructure.entities.NotificationEntity

data class Notifications (
    val id: String,
    val type: NotificationType,
    var status: NotificationStatus,
    val contentId: String,
    val payload: NotificationPayload,
    val date: Long,
    val recipient: String
) {
    fun toEntity(): NotificationEntity {
        return NotificationEntity(
            id = id,
            type = type,
            status = status,
            contentId = contentId,
            payload = payload,
            date = date,
            recipient = recipient
        )
    }

}