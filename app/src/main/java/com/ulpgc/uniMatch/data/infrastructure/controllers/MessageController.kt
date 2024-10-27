package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.models.Message
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MessageController {

    @GET("messages")
    suspend fun getMessages(
        @Header("Authorization") authToken: String,
        @Query("before") lastMessageTime: Long?,
        @Query("limit") limit: Int = 50
    ): ApiResponse<List<Message>>
}