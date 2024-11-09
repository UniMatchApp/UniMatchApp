package com.ulpgc.uniMatch.data.infrastructure.services.user

import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.models.User
import com.ulpgc.uniMatch.data.infrastructure.mocks.UserMock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MockUserService : UserService {
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

    override suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun reportUser(userId: String, reportedUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun blockUser(userId: String, blockedUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun forgotPassword(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun verifyCode(email: String, code: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun resetPassword(email: String, newPassword: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
