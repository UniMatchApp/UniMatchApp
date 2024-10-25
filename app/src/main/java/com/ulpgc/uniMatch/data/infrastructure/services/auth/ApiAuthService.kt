package com.ulpgc.uniMatch.data.infrastructure.services.auth

import android.util.Log
import com.ulpgc.uniMatch.data.application.ApiEndpoints
import com.ulpgc.uniMatch.data.application.LoginRequest
import com.ulpgc.uniMatch.data.application.LoginResponse
import com.ulpgc.uniMatch.data.application.RegisterRequest
import com.ulpgc.uniMatch.data.application.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiAuthService(private val apiEndpoints: ApiEndpoints) : AuthService {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.i("ApiAuthService", "Logging in with email: $email, password: $password")
                val response = apiEndpoints.login(LoginRequest(email, password))
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
                val response = apiEndpoints.register(RegisterRequest(email, password))
                if (response.success) {
                    Result.success(response.value!!)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Registration failed: ${e.message}"))
            }
        }
    }
}
