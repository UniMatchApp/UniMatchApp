package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationController {

    @GET("notifications")
    suspend fun getNotifications(): ApiResponse<List<Notifications>>

    @POST("notifications/seen/{notificationId}")
    suspend fun markNotificationAsSeen(notificationId: String): ApiResponse<Unit>

    @DELETE("notifications/{notificationId}")
    suspend fun deleteNotification(notificationId: String): ApiResponse<Unit>

    @DELETE("notifications")
    suspend fun deleteAllNotifications(): ApiResponse<Unit>
}