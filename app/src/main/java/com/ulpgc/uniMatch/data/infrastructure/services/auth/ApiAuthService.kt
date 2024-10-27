package com.ulpgc.uniMatch.data.infrastructure.services.auth

import android.util.Log


import com.ulpgc.uniMatch.data.application.services.AuthService
import com.ulpgc.uniMatch.data.application.services.LoginRequest
import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.RegisterRequest
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import com.ulpgc.uniMatch.data.domain.models.User
import com.ulpgc.uniMatch.data.infrastructure.controllers.AuthController
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureStorage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiAuthService(
    private val authController: AuthController, private val secureStorage: SecureStorage
) : AuthService {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.i("ApiAuthService", "Logging in with email: $email, password: $password")
                val response = authController.login(LoginRequest(email, password))
                Log.i("ApiAuthService", "Login response: $response")
                if (response.success) {
                    Result.success(response.value!!)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Log.e("ApiAuthService", "Login failed: ${e.message}")
                Result.failure(Throwable("Login failed: ${e.message}"))
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authController.register(RegisterRequest(email, password))
                if (response.success) {
                    if (response.value == null) {
                        return@withContext Result.failure(Throwable("Registration failed: response value is null"))
                    }
                    secureStorage.saveUser(
                        response.value.user.id,
                        response.value.user.email,
                        response.value.user.registrationDate,
                        response.value.user.blockedUsers
                    )
                    Result.success(response.value)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Registration failed: ${e.message}"))
            }
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return withContext(Dispatchers.IO) {
            try {
                val user = secureStorage.getUser()
                Result.success(user)
            } catch (e: Exception) {
                Log.e("ApiAuthService", "Failed to get current user: ${e.message}")
                Result.failure(Throwable("Failed to get current user: ${e.message}"))
            }
        }
    }
}
