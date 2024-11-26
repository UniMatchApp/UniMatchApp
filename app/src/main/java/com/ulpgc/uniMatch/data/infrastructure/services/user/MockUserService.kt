package com.ulpgc.uniMatch.data.infrastructure.services.user

import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.models.User
import com.ulpgc.uniMatch.data.infrastructure.mocks.UserMock
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MockUserService : UserService {
    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(
                    LoginResponse(
                        "mock_token",
                        UserMock.createMockLoggedUser()
                    )
                )
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(
                    RegisterResponse(
                        "mock_token",
                        UserMock.createMockUser()
                    )
                )
            }
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(
                    UserMock.createMockLoggedUser()
                )
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(Unit)
            }
        }
    }

    override suspend fun reportUser(
        reportedUserId: String,
        predefinedReason: String,
        comment: String?
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(Unit)
            }
        }
    }

    override suspend fun blockUser(blockedUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(Unit)
            }
        }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success("mock_code")
            }
        }
    }

    override suspend fun verifyCode(email: String, code: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(true)
            }
        }
    }

    override suspend fun resetPassword(newPassword: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(true)
            }
        }
    }

    override suspend fun resendCode(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(true)
            }
        }
    }

    override suspend fun deleteAccount(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Result.success(Unit)
            }
        }
    }
}
