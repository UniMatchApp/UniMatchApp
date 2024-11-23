package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.User

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)

data class LoginResponse(val token: String, val user: User)
data class RegisterResponse(val token: String, val user: User)

data class PasswordRequest(val password: String)

data class ReportRequest(val predefinedReason: String, val comment: String?)


interface UserService {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun register(email: String, password: String): Result<RegisterResponse>
    suspend fun getCurrentUser(): Result<User?>
    suspend fun logout(): Result<Unit>
    suspend fun reportUser(userId: String, reportedUserId: String, predefinedReason: String, comment: String?): Result<Unit>
    suspend fun blockUser(userId: String, blockedUserId: String): Result<Unit>
    suspend fun forgotPassword(email: String): Result<String>
    suspend fun verifyCode(email: String, code: String): Result<Boolean>
    suspend fun resetPassword(userId: String, newPassword: String): Result<Boolean>
    suspend fun resendCode(email: String): Result<Boolean>
    suspend fun deleteAccount(userId: String): Result<Unit>
}