package com.ulpgc.uniMatch.data.infrastructure.services.notification

import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.infrastructure.mocks.NotificationMock
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest

class MockNotificationService(
) : NotificationsService {

    override suspend fun getNotifications(userId: String): Result<List<Notifications>> {
        return safeRequest {
            val notifications = NotificationMock.getNotificationsForUser(userId)
            return@safeRequest notifications
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return safeRequest {
            NotificationMock.markNotificationAsRead(notificationId)
            Result.success(Unit)
        }
    }

    override suspend fun deleteNotification(notificationId: String): Result<Unit> {
        return safeRequest {
            NotificationMock.deleteNotification(notificationId)
            Result.success(Unit)
        }
    }

    override suspend fun deleteAllNotifications(userId: String): Result<Unit> {
        return safeRequest {
            NotificationMock.deleteAllNotifications(userId)
            Result.success(Unit)
        }
    }
}
