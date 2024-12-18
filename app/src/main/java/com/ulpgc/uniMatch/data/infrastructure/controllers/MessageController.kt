package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.ModifyMessageDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageController {

    @GET("messages")
    suspend fun getMessages(
        @Query("after") lastMessageTime: Long = 0,
        @Query("limit") limit: Int = 50
    ): ApiResponse<List<Message>>

    @POST("messages")
    suspend fun sendMessage(
        @Body message: Message
    ): ApiResponse<Message>

    @PUT("messages/{messageId}")
    suspend fun modifyMessage(
        @Path("messageId") messageId: String,
        @Body message: ModifyMessageDTO
    ): ApiResponse<Message>

    @POST("messages/read/{messageId}")
    suspend fun messageHasBeenRead(
        @Path("messageId") messageId: String
    ): ApiResponse<Unit>

    @POST("messages/received/{messageId}")
    suspend fun messageHasBeenReceived(
        @Path("messageId") messageId: String
    ): ApiResponse<Unit>


    @DELETE("messages/{messageId}")
    suspend fun deleteMessage(
        @Path("messageId") messageId: String
    ): ApiResponse<Unit>
}