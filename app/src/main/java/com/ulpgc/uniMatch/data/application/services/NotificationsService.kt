package com.ulpgc.uniMatch.data.application.services

import NotificationPayload
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import java.time.Instant

data class NotificationResponse(
    val id: String,
    val status: String,
    val date: String,
    val recipient: String,
    val contentId: String,
    val payload: NotificationPayload
)


fun NotificationResponse.toDomainModel(): Notifications {
    return Notifications(
        id = id,
        status = NotificationStatus.valueOf(status),
        contentId = contentId,
        payload = payload,
        date = parseDateToTimestamp(date),
        recipient = recipient
    )
}

fun parseDateToTimestamp(date: String): Long {
    return try {
        Instant.parse(date).toEpochMilli()
    } catch (e: Exception) {
        0L
    }
}


interface NotificationsService {
    suspend fun getNotifications(userId: String): Result<List<Notifications>>
    suspend fun markNotificationAsRead(notificationId: String): Result<Unit>
    suspend fun deleteNotification(notificationId: String): Result<Unit>
    suspend fun deleteAllNotifications(userId: String): Result<Unit>
}