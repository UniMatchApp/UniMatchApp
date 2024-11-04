package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.notification.Notifications

interface NotificationsService {
    suspend fun getNotifications(userId: String): Result<List<Notifications>>
    suspend fun markNotificationAsRead(notificationId: String): Result<Unit>
    suspend fun deleteNotification(notificationId: String): Result<Unit>
    suspend fun deleteAllNotifications(userId: String): Result<Unit>
}