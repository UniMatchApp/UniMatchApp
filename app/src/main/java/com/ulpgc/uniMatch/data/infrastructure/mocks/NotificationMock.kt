package com.ulpgc.uniMatch.data.infrastructure.mocks

import AppNotificationPayload
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.enums.NotificationType

object NotificationMock {
    private val notificationsList = mutableListOf<Notifications>()

    init {
        notificationsList.add(
            Notifications(
                id = "1",
                type = NotificationType.APP,
                status = NotificationStatus.UNREAD,
                contentId = "content1",
                payload = AppNotificationPayload("1", "New Message", "You have a new message"),
                date = System.currentTimeMillis(),
                recipient = "1"
            )
        )
        notificationsList.add(
            Notifications(
                id = "2",
                type = NotificationType.APP,
                status = NotificationStatus.UNREAD,
                contentId = "content2",
                payload = AppNotificationPayload("2", "Friend Request", "John Doe sent you a friend request"),
                date = System.currentTimeMillis(),
                recipient = "1"
            )
        )
    }

    fun getNotificationsForUser(userId: String): List<Notifications> {
        return notificationsList.filter { it.recipient == userId }
    }

    fun markNotificationAsRead(notificationId: String) {
        notificationsList.find { it.id == notificationId }?.let {
            it.status = NotificationStatus.READ
        }
    }

    fun deleteNotification(notificationId: String) {
        notificationsList.removeAll { it.id == notificationId }
    }

    fun deleteAllNotifications(userId: String) {
        notificationsList.removeAll { it.recipient == userId }
    }
}
