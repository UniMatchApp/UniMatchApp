package com.ulpgc.uniMatch.data.infrastructure.services.auth

import com.ulpgc.uniMatch.data.application.LoginResponse
import com.ulpgc.uniMatch.data.application.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MockAuthService() : AuthService {
    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(LoginResponse("mock_token", "mock_user_id"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(RegisterResponse(true))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
