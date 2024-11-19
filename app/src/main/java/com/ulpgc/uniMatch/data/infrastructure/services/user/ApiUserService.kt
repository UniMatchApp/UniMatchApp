package com.ulpgc.uniMatch.data.infrastructure.services.user


import android.util.Log
import com.ulpgc.uniMatch.data.application.services.LoginRequest
import com.ulpgc.uniMatch.data.application.services.LoginResponse
import com.ulpgc.uniMatch.data.application.services.PasswordRequest
import com.ulpgc.uniMatch.data.application.services.RegisterRequest
import com.ulpgc.uniMatch.data.application.services.RegisterResponse
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.models.User
import com.ulpgc.uniMatch.data.infrastructure.controllers.UserController
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ApiUserService(
    private val userController: UserController, private val secureStorage: SecureStorage
) : UserService {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.i("ApiAuthService", "Logging in with email: $email, password: $password")
                val response = userController.login(LoginRequest(email, password))
                Log.i("ApiAuthService", "Login response: $response")
                if (response.success) {
                    Result.success(response.value!!)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("ApiAuthService", "HTTP error: $errorMessage")
                Result.failure(Throwable("Login failed: $errorMessage"))
            } catch (e: Exception) {
                Log.e("ApiAuthService", "Login failed: ${e.message}")
                Result.failure(Throwable("Login failed: ${e.message}"))
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
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
                        response.value.user.registered
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

    override suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                secureStorage.clearUser()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(Throwable("Failed to logout: ${e.message}"))
            }
        }
    }

    override suspend fun reportUser(userId: String, reportedUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userController.reportUser(userId, reportedUserId)
                if (response.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Failed to report user: ${e.message}"))
            }
        }
    }

    override suspend fun blockUser(userId: String, blockedUserId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userController.blockUser(userId, blockedUserId)
                if (response.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Failed to block user: ${e.message}"))
            }
        }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userController.forgotPassword(email)
                if (response.success) {
                    Result.success(response.value!!)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Failed to send forgot password email: ${e.message}"))
            }
        }
    }

    override suspend fun verifyCode(userId: String, code: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userController.verifyCode(userId, code)
                Log.i("ApiAuthService", "Verify code response: $response")
                if (response.success) {
                    Result.success(true)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Log.e("ApiAuthService", "Failed to verify code: ${e.message}")
                Result.failure(Throwable("Failed to verify code: ${e.message}"))
            }
        }
    }

    override suspend fun resetPassword(userId: String, newPassword: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userController.resetPassword(userId, PasswordRequest(newPassword))
                if (response.success) {
                    Result.success(true)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Failed to reset password: ${e.message}"))
            }
        }
    }

    override suspend fun resendCode(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userController.resendCode(email)
                if (response.success) {
                    Result.success(true)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Failed to resend code: ${e.message}"))
            }
        }
    }


}
