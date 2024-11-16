package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.LoginRequest
import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.RegisterRequest
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserController {

    @POST("users/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>

    @POST("users")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("users/{id}/report/{targetId}")
    suspend fun reportUser(@Path("id") userId: String, @Path("targetId") reportedUserId: String): ApiResponse<Unit>

    @POST("users/{id}/block/{targetId}")
    suspend fun blockUser(@Path("id") userId: String, @Path("targetId") blockedUserId: String): ApiResponse<Unit>

    @POST("users/auth/{email}/forgot-password")
    suspend fun forgotPassword(@Path("email") email: String): ApiResponse<String>

    @POST("users/auth/{id}/verify-code/{code}")
    suspend fun verifyCode(@Path("id") userId: String, @Path("code") code: String): ApiResponse<Boolean>

    @POST("users/{id}/password")
    suspend fun resetPassword(@Path("id") userId: String, @Body password: String): ApiResponse<Boolean>
}