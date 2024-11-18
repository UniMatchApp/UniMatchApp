package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.io.File

open class UserViewModel(
    private val userService: UserService,
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel
) : ViewModel() {

    private val _verifyCodeResult = MutableStateFlow<Boolean?>(null)
    var verifyCodeResult: StateFlow<Boolean?> = _verifyCodeResult

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> get() = _authState

    private val _forgotPasswordResult = MutableStateFlow<Boolean?>(null)
    val forgotPasswordResult: StateFlow<Boolean?> = _forgotPasswordResult

    private val _forgotPasswordUserId = MutableStateFlow<String?>(null)
    val forgotPasswordUserId: StateFlow<String?> = _forgotPasswordUserId

    private val _resetPasswordResult = MutableStateFlow<Boolean?>(null)
    val resetPasswordResult: StateFlow<Boolean?> = _resetPasswordResult

    val userId: String?
        get() = (authState.value as? AuthState.Authenticated)?.user?.id

    private var authToken: String? = null

    private val _profileCreated = MutableStateFlow<Boolean>(false)
    val profileCreated: StateFlow<Boolean> = _profileCreated

    private var _temporaryEmail = MutableStateFlow<String?>(null)
    val temporaryEmail: StateFlow<String?> = _temporaryEmail

    private var _temporaryPassword = MutableStateFlow<String?>(null)
    val temporaryPassword: StateFlow<String?> = _temporaryPassword

    private var _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    private var _registeredUserId = MutableStateFlow<String?>(null)
    val registeredUserId: StateFlow<String?> = _registeredUserId

    private var _loginUserId = MutableStateFlow<String?>(null)
    val loginUserId: StateFlow<String?> = _loginUserId

    fun login(email: String, password: String) {

        viewModelScope.launch {
            val result = userService.login(email.trim(), password)
            result.onSuccess { loginResponse ->
                authToken = loginResponse.token
                Log.i(("Login response"), loginResponse.toString())
                if (loginResponse.user.registered) {
                    Log.i("UserViewModel", "User is registered")
                    _authState.value = AuthState.Authenticated(loginResponse.user)
                } else {
                    Log.i("UserViewModel", "User is not registered")
                    _authState.value = AuthState.Unauthenticated
                    _loginUserId.value = loginResponse.user.id
                }
                _email.value = email
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun register() {
        val email = _temporaryEmail.value
        val password = _temporaryPassword.value

        if (email == null || password == null) {
            errorViewModel.showError("Email or password is missing")
            return
        }

        viewModelScope.launch {
            val result = userService.register(email, password)
            result.onSuccess {
                _email.value = email
                _registeredUserId.value = it.user.id
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
            }
            _temporaryEmail.value = null
            _temporaryPassword.value = null
        }
    }

    fun logout() {
        authToken = null
        _authState.value = AuthState.Unauthenticated
        _email.value = null
    }

    fun forgotPassword(email: String): Boolean {
        viewModelScope.launch {
            val result = userService.forgotPassword(email)
            result.onSuccess {
                _forgotPasswordResult.value = true
                _temporaryEmail.value = email
                _forgotPasswordUserId.value = it
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _forgotPasswordResult.value = false
            }
        }
        return _forgotPasswordResult.value ?: false
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            val result = userService.verifyCode(email, code)
            result.onSuccess {
                _verifyCodeResult.value = true
            }.onFailure {
                _verifyCodeResult.value = false
                errorViewModel.showError(it.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetPassword(userId: String, newPassword: String) {
        viewModelScope.launch {
            val result = userService.resetPassword(userId, newPassword)
            result.onSuccess {
                _resetPasswordResult.value = true
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _resetPasswordResult.value = false
            }
        }
    }

    fun resendCode(email: String) {
        viewModelScope.launch {
            val result = userService.forgotPassword(email)
            result.onSuccess {
                _forgotPasswordResult.value = true
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _forgotPasswordResult.value = false
            }
        }
    }

    fun createProfile(
        userId: String,
        fullName: String,
        age: Int,
        aboutMe: String,
        gender: Gender,
        sexualOrientation: SexualOrientation,
        relationshipType: RelationshipType,
        birthday: String,
        location: Pair<Double, Double>?,
        profileImageUri: Uri
    ) {
        viewModelScope.launch {
            val result = profileService.createProfile(
                userId,
                fullName,
                age,
                aboutMe,
                gender,
                sexualOrientation,
                relationshipType,
                birthday,
                location,
                profileImageUri
            )
            result.onSuccess {
                _profileCreated.value = true
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _profileCreated.value = false
            }
        }
    }

    fun resetVerificationResult() {
        _verifyCodeResult.value = null
    }

    fun resetRegisteredUserId() {
        _registeredUserId.value = null
    }

    fun resetLoginUserId() {
        _loginUserId.value = null
    }

    fun resetTemporaryEmail() {
        _temporaryEmail.value = null
    }

    fun resetEmail() {
        _email.value = null
    }

    fun resetForgotPasswordResult() {
        _forgotPasswordResult.value = null
    }

    fun resetForgotPasswordUserId() {
        _forgotPasswordUserId.value = null
    }

    fun resetPasswordResult() {
        _resetPasswordResult.value = null
    }

    fun partialRegistration(email: String, password: String) {
        _temporaryEmail.value = email
        _temporaryPassword.value = password
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}


sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()

}

