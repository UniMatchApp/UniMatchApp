package com.ulpgc.uniMatch.data.infrastructure.viewModels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


open class AuthViewModel(
    private val userService: UserService,
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel
) : ViewModel() {

    private val _verifyCodeResult = MutableStateFlow<Boolean?>(null)
    var verifyCodeResult: StateFlow<Boolean?> = _verifyCodeResult

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> get() = _authState

    private val _forgotPasswordResult = MutableStateFlow<Boolean>(true)
    val forgotPasswordResult: StateFlow<Boolean> = _forgotPasswordResult

    private val _resetPasswordResult = MutableStateFlow<Boolean>(true)
    val resetPasswordResult: StateFlow<Boolean> = _resetPasswordResult

    val userId: String?
        get() = (_authState.value as? AuthState.Authenticated)?.user?.id

    private var authToken: String? = null

    private val _registeredUserId = MutableStateFlow<String?>(null)
    val registeredUserId: StateFlow<String?> = _registeredUserId

    private val _profileCreated = MutableStateFlow<Boolean>(false)
    val profileCreated: StateFlow<Boolean> = _profileCreated


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userService.login(email, password)
            result.onSuccess { loginResponse ->
                authToken = loginResponse.token
                _authState.value = AuthState.Authenticated(loginResponse.user)
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = userService.register(email, password)
            result.onSuccess {
                _registeredUserId.value = it.user.id
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _registeredUserId.value = null
            }
        }
    }

    fun logout() {
        authToken = null
        _authState.value = AuthState.Unauthenticated
    }

    fun forgotPassword(email: String): Boolean {
        viewModelScope.launch {
            val result = userService.forgotPassword(email)
            result.onSuccess {
                _forgotPasswordResult.value = true
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _forgotPasswordResult.value = false
            }
        }
        return _forgotPasswordResult.value
    }

    fun verifyCode(userId: String, code: String) {
        viewModelScope.launch {
            val result = userService.verifyCode(userId, code)
            result.onSuccess {
                _verifyCodeResult.value = true
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _verifyCodeResult.value = false
            }
        }
    }

    fun resetPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            val result = userService.resetPassword(email, newPassword)
            result.onSuccess {
                _resetPasswordResult.value = true
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _resetPasswordResult.value = false
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
        profileImageUri: String?
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
}


sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()

}

