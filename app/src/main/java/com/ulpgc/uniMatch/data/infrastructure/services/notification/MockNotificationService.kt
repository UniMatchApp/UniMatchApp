package com.ulpgc.uniMatch.data.infrastructure.services.notification

import androidx.compose.ui.platform.LocalContext
import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.infrastructure.mocks.NotificationMock

class MockNotificationService(
) : NotificationsService {

    override suspend fun getNotifications(userId: String): Result<List<Notifications>> {
        return try {
            val notifications = NotificationMock.getNotificationsForUser(userId)
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return try {
            NotificationMock.markNotificationAsRead(notificationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNotification(notificationId: String): Result<Unit> {
        return try {
            NotificationMock.deleteNotification(notificationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAllNotifications(userId: String): Result<Unit> {
        return try {
            NotificationMock.deleteAllNotifications(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
