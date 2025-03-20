package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.ModifyMessageDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageController {

    @GET("messages")
    suspend fun getMessages(
        @Query("after") lastMessageTime: Long = 0,
        @Query("limit") limit: Int = 50
    ): ApiResponse<List<Message>>

    @Multipart
    @POST("messages")
    suspend fun sendMessage(
        @Part("messageId") messageId: RequestBody,
        @Part("content") content: RequestBody,
        @Part("senderId") senderId: RequestBody,
        @Part("recipientId") recipientId: RequestBody,
        @Part attachment: MultipartBody.Part? = null,
        @Part("receptionStatus") receptionStatus: RequestBody,
        @Part("contentStatus") contentStatus: RequestBody,
        @Part("deletedStatus") deletedStatus: RequestBody,
        @Part("createdAt") createdAt: RequestBody,
        @Part("updatedAt") updatedAt: RequestBody
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
        @Path("messageId") messageId: String,
    ): ApiResponse<Unit>

    @DELETE("messages/user/{userId}")
    suspend fun deleteAllMessages(
        @Path("userId") userId: String
    ): ApiResponse<Unit>
}