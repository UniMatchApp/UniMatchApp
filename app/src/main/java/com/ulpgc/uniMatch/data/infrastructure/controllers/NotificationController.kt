package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.NotificationResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationController {

    @GET("notifications")
    suspend fun getNotifications(): ApiResponse<List<NotificationResponse>>

    @POST("notifications/seen/{notificationId}")
    suspend fun markNotificationAsSeen(
        @Path("notificationId") notificationId: String
    ): ApiResponse<Unit>

    @DELETE("notifications/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") notificationId: String
    ): ApiResponse<Unit>

    @DELETE("notifications")
    suspend fun deleteAllNotifications(): ApiResponse<Unit>
}