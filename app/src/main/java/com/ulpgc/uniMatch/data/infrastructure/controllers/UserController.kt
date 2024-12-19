package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.LoginRequest
import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.PasswordRequest
import com.ulpgc.uniMatch.data.application.services.RegisterRequest
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import com.ulpgc.uniMatch.data.application.services.ReportRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserController {

    @POST("users/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>

    @POST("users")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("users/report/{targetId}")
    suspend fun reportUser(@Path("targetId") reportedUserId: String, @Body reportRequest: ReportRequest): ApiResponse<Unit>

    @POST("users/block/{targetId}")
    suspend fun blockUser(@Path("targetId") blockedUserId: String): ApiResponse<Unit>

    @POST("users/auth/{email}/forgot-password")
    suspend fun forgotPassword(@Path("email") email: String): ApiResponse<String>

    @POST("users/auth/{email}/verify-code/{code}")
    suspend fun verifyCode(@Path("email") userId: String, @Path("code") code: String): ApiResponse<Boolean>

    @PUT("users/password/{id}")
    suspend fun resetPassword(@Body passwordRequest: PasswordRequest, @Path("id") userId: String): ApiResponse<Unit>

    @POST("users/auth/{email}/resend-code")
    suspend fun resendCode(@Path("email") email: String): ApiResponse<Boolean>

    @DELETE("users")
    suspend fun deleteAccount(): ApiResponse<Unit>
}