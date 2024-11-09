package com.ulpgc.uniMatch.data.infrastructure.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


open class AuthViewModel(
    private val userService: UserService,
    private val errorViewModel: ErrorViewModel
) : ViewModel() {

    private val _verifyCodeResult = MutableStateFlow<Boolean>(true)
    val verifyCodeResult: StateFlow<Boolean> = _verifyCodeResult

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> get() = _authState

    private val _forgotPasswordResult = MutableStateFlow<Boolean>(true)
    val forgotPasswordResult: StateFlow<Boolean> = _forgotPasswordResult

    private val _resetPasswordResult = MutableStateFlow<Boolean>(true)
    val resetPasswordResult: StateFlow<Boolean> = _resetPasswordResult

    val userId: String?
        get() = (_authState.value as? AuthState.Authenticated)?.user?.id

    private var authToken: String? = null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userService.login(email, password)
            result.onSuccess { loginResponse ->
                authToken = loginResponse.token
                _authState.value = AuthState.Authenticated(loginResponse.user)
            }.onFailure {
                errorViewModel.showError(
                    it.message ?: "Unknown error occurred"
                ) // Usar el ErrorViewModel
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = userService.register(email, password)
            result.onSuccess {
                _authState.value = AuthState.Authenticated(it.user)
            }.onFailure {
                errorViewModel.showError(it.message ?: "Unknown error occurred")
                _authState.value = AuthState.Unauthenticated
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

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            val result = userService.verifyCode(email, code)
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

}


sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()

}

