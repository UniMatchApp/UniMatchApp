package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.models.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageController {

    @GET("messages")
    suspend fun getMessages(
        @Header("Authorization") authToken: String,
        @Query("before") lastMessageTime: Long?,
        @Query("limit") limit: Int = 50
    ): ApiResponse<List<Message>>

    @POST("messages")
    suspend fun sendMessage(
        @Header("Authorization") authToken: String,
        @Body message: Message
    ): ApiResponse<Unit>

    @GET("matching/likes/:userId")
    suspend fun getMatchingUserIds(
        @Query("userId") userId: String
    ): ApiResponse<List<String>>

}