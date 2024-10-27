package com.ulpgc.uniMatch.data.infrastructure.services.auth

import com.ulpgc.uniMatch.data.application.services.AuthService
import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import com.ulpgc.uniMatch.data.domain.models.User
import com.ulpgc.uniMatch.data.infrastructure.mocks.UserMock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MockAuthService : AuthService {
    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(
                    LoginResponse(
                        "mock_token",
                        UserMock.createMockLoggedUser()
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(
                    RegisterResponse(
                        "mock_token",
                        UserMock.createMockUser()
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(
                    UserMock.createMockLoggedUser()
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
