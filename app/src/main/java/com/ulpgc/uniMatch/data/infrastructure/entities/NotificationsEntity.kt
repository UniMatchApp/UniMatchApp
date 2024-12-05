package com.ulpgc.uniMatch.data.infrastructure.entities

import NotificationPayload
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val status: NotificationStatus,
    val contentId: String,
    val payload: String,
    val date: Long,
    val recipient: String
) {
    companion object {
        private val gson = GsonBuilder()
            .registerTypeAdapter(NotificationPayload::class.java, NotificationPayloadAdapter())
            .create()

        fun fromDomain(notification: Notifications): NotificationEntity {
            return NotificationEntity(
                id = notification.id,
                status = notification.status,
                contentId = notification.contentId,
                payload = gson.toJson(notification.payload),
                date = notification.date,
                recipient = notification.recipient
            )
        }

        fun toDomain(notificationEntity: NotificationEntity): Notifications {
            return Notifications(
                id = notificationEntity.id,
                status = notificationEntity.status,
                contentId = notificationEntity.contentId,
                payload = gson.fromJson(notificationEntity.payload, NotificationPayload::class.java),
                date = notificationEntity.date,
                recipient = notificationEntity.recipient
            )
        }
    }
}
