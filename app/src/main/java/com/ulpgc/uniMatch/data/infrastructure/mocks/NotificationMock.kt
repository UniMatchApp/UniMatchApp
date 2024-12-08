package com.ulpgc.uniMatch.data.infrastructure.mocks

import AppNotificationPayload
import com.ulpgc.uniMatch.data.domain.models.notification.Notification
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.enums.NotificationType

object NotificationMock {
    private val notificationList = mutableListOf<Notification>()

    init {
        notificationList.add(
            Notification(
                id = "1",
                status = NotificationStatus.SENT,
                contentId = "content1",
                payload = AppNotificationPayload("1", "New Message", "You have a new message"),
                date = System.currentTimeMillis(),
                recipient = "1"
            )
        )
        notificationList.add(
            Notification(
                id = "2",
                status = NotificationStatus.SENT,
                contentId = "content2",
                payload = AppNotificationPayload("2", "Friend Request", "John Doe sent you a friend request"),
                date = System.currentTimeMillis(),
                recipient = "1"
            )
        )
    }

    fun getNotificationsForUser(userId: String): List<Notification> {
        return notificationList.filter { it.recipient == userId }
    }

    fun markNotificationAsRead(notificationId: String) {
        notificationList.find { it.id == notificationId }?.let {
            it.status = NotificationStatus.READ
        }
    }

    fun deleteNotification(notificationId: String) {
        notificationList.removeAll { it.id == notificationId }
    }

    fun deleteAllNotifications(userId: String) {
        notificationList.removeAll { it.recipient == userId }
    }
}
