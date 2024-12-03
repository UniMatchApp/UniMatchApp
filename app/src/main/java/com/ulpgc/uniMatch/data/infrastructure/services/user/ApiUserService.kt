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
import com.ulpgc.uniMatch.ui.screens.shared.safeApiCall
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
                return@safeRequest response.value

            }
        }
    }

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.register(RegisterRequest(email, password))
                if (!response.success) {
                    throw Exception(response.errorMessage ?: "Unknown error occurred")
                }
                if (response.value == null) {
                    throw IllegalStateException("Response value is null")
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
                return@safeRequest response.value
            }
        }
    }

    override suspend fun deleteAccount(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val response = userController.deleteAccount()
                if (!response.success) {
                    throw Exception(response.errorMessage ?: "Unknown error occurred")
                }
                secureStorage.clearUser()
            }
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                val user = secureStorage.getUser()
                return@safeRequest user
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeRequest {
                secureStorage.clearUser()
            }
        }
    }

    override suspend fun reportUser(
        reportedUserId: String,
        predefinedReason: String,
        comment: String?
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userController.reportUser(
                    reportedUserId,
                    ReportRequest(predefinedReason, comment)
                )
            }
        }.mapCatching {  }
    }

    override suspend fun blockUser(blockedUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userController.blockUser(blockedUserId)
            }
        }.mapCatching { }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userController.forgotPassword(email)
            }
        }.mapCatching { it ?: throw IllegalStateException("Response value is null") }
    }

    override suspend fun verifyCode(email: String, code: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userController.verifyCode(email, code)
            }
        }.mapCatching {  }
    }

    override suspend fun resetPassword(newPassword: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userController.resetPassword(PasswordRequest(newPassword))
            }
        }.mapCatching { Unit }
    }

    override suspend fun resendCode(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            safeApiCall { userController.resendCode(email) }
        }.mapCatching { it ?: throw IllegalStateException("Response value is null") }
    }

}
