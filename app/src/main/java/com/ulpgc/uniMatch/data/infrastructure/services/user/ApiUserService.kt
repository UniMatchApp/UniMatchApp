package com.ulpgc.uniMatch.data.infrastructure.services.user


import android.util.Log
import com.ulpgc.uniMatch.data.application.services.LoginRequest
import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.PasswordRequest
import com.ulpgc.uniMatch.data.application.services.RegisterRequest
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import com.ulpgc.uniMatch.data.application.services.ReportRequest
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.models.User
import com.ulpgc.uniMatch.data.infrastructure.controllers.UserController
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureStorage
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ApiUserService(
    private val userController: UserController, private val secureStorage: SecureStorage
) : UserService {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                Log.i("ApiAuthService", "Logging in with email: $email, password: $password")
                val response = userController.login(LoginRequest(email, password))
                Log.i("ApiAuthService", "Login response: $response")
                if (!response.success || response.value == null) {
                    return@withContext Result.failure(
                        Throwable(
                            response.errorMessage ?: (("Unknown error occurred") +
                                    (response.value == null).let { "and response value is null" })
                        )
                    )

                }
                secureStorage.saveUser(
                    response.value.user.id,
                    response.value.user.email,
                    response.value.user.registrationDate,
                    response.value.user.blockedUsers,
                    response.value.user.reportedUsers,
                    response.value.user.registered,
                    response.value.token
                )
                Result.success(response.value)

            }
        }
    }

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.register(RegisterRequest(email, password))
                if (response.success) {
                    if (response.value == null) {
                        return@withContext Result.failure(Throwable("Registration failed: response value is null"))
                    }
                    secureStorage.saveUser(
                        response.value.user.id,
                        response.value.user.email,
                        response.value.user.registrationDate,
                        response.value.user.blockedUsers,
                        response.value.user.reportedUsers,
                        response.value.user.registered,
                        response.value.token
                    )
                    Result.success(response.value)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun deleteAccount(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.deleteAccount(userId)
                if (response.success) {
                    secureStorage.clearUser()
                    Result.success(Unit)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val user = secureStorage.getUser()
                Result.success(user)
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                secureStorage.clearUser()
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
                val response = userController.reportUser(
                    reportedUserId,
                    ReportRequest(predefinedReason, comment)
                )
                if (response.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun blockUser(blockedUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.blockUser(blockedUserId)
                if (response.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.forgotPassword(email)
                if (response.success) {
                    Result.success(response.value!!)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun verifyCode(email: String, code: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.verifyCode(email, code)
                Log.i("ApiAuthService", "Verify code response: $response")
                if (response.success) {
                    Result.success(true)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun resetPassword(newPassword: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.resetPassword(PasswordRequest(newPassword))
                if (response.success) {
                    Result.success(true)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    override suspend fun resendCode(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.resendCode(email)
                if (response.success) {
                    Result.success(true)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            }
        }
    }


}
