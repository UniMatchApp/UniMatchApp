package com.ulpgc.uniMatch.data.application

import com.ulpgc.uniMatch.data.infrastructure.entities.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)

data class LoginResponse(val token: String, val userId: String)
data class RegisterResponse(val success: Boolean)

interface ApiEndpoints {

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    // Obtener los mensajes más antiguos que el último timestamp conocido
    @GET("messages")
    suspend fun getMessages(
        @Header("Authorization") authToken: String,
        @retrofit2.http.Query("before") lastMessageTime: Long?,  // Mensajes anteriores a este timestamp
        @retrofit2.http.Query("limit") limit: Int = 50           // Límite de mensajes por página
    ): ApiResponse<List<Message>>




}
