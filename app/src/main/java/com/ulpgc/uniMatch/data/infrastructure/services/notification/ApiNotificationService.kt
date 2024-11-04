package com.ulpgc.uniMatch.data.infrastructure.services.notification

import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.infrastructure.controllers.NotificationController

class ApiNotificationService (
    private val notificationController: NotificationController
) : NotificationsService {

    override suspend fun getNotifications(userId: String): Result<List<Notifications>> {
        TODO("Not yet implemented")
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotification(notificationId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllNotifications(userId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}