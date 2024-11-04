package com.ulpgc.uniMatch.data.infrastructure.services.notification

import android.app.Notification
import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.infrastructure.controllers.NotificationController

class ApiNotificationService (
    private val notificationController: NotificationController
) : NotificationsService {

    override suspend fun getNotifications(userId: String): Result<List<Notification>> {
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