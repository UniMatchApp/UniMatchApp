package com.ulpgc.uniMatch.data.infrastructure.services.notification

import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.infrastructure.controllers.NotificationController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.NotificationsDao
import com.ulpgc.uniMatch.data.infrastructure.entities.NotificationEntity
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList

class ApiNotificationService (
    private val notificationController: NotificationController,
    private val notificationDao: NotificationsDao
) : NotificationsService {

    override suspend fun getNotifications(userId: String): Result<List<Notifications>> {
        return safeRequest {
            val response = notificationController.getNotifications()

            if (!response.success) {
                return@safeRequest notificationDao.getAllNotifications(userId).map(NotificationEntity::toDomain)
            }

            val notifications = response.value.orEmpty()

            notifications.forEach { notification ->
                val notificationEntity = NotificationEntity.fromDomain(notification)
                notificationDao.insertNotifications(listOf(notificationEntity))
            }

            return@safeRequest notifications
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return safeRequest {
            notificationDao.updateNotificationStatus(notificationId, NotificationStatus.READ)
            val response = notificationController.markNotificationAsSeen(notificationId)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun deleteNotification(notificationId: String): Result<Unit> {
        return safeRequest {
            notificationDao.deleteNotification(notificationId)
            val response = notificationController.deleteNotification(notificationId)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun deleteAllNotifications(userId: String): Result<Unit> {
        return safeRequest {
            notificationDao.deleteAllNotifications(userId)
            val response = notificationController.deleteAllNotifications()

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

        }
    }

}