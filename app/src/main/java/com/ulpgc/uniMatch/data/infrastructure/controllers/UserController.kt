package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.LoginRequest
import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.RegisterRequest
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserController {

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    // Denunciar a un usuario
    @POST("users/report")
    suspend fun reportUser(@Query("userId") userId: String, @Query("reportedUserId") reportedUserId: String): ApiResponse<Unit>

    // Bloquear a un usuario
    @POST("users/block")
    suspend fun blockUser(@Query("userId") userId: String, @Query("blockedUserId") blockedUserId: String): ApiResponse<Unit>
}