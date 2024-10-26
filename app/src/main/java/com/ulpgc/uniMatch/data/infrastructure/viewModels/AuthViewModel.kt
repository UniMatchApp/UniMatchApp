package com.ulpgc.uniMatch.data.infrastructure.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.infrastructure.entities.User
import com.ulpgc.uniMatch.data.infrastructure.services.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


open class AuthViewModel(
    private val authService: AuthService,
    private val errorViewModel: ErrorViewModel // Inyectamos el ErrorViewModel
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Authenticated(User.createMockLoggedUser()))
    val authState: StateFlow<AuthState> get() = _authState

    val userId: String?
        get() = (_authState.value as? AuthState.Authenticated)?.user?.id

    private var authToken: String? = null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authService.login(email, password)
            result.onSuccess { loginResponse ->
                authToken = loginResponse.token
                _authState.value = AuthState.Authenticated(User.createMockUser())
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
            val result = authService.register(email, password)
            result.onSuccess {
                _authState.value = AuthState.Authenticated(User.createMockUser())
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

    fun forgotPassword(email: String) {
        TODO("Not yet implemented")

    }

}


sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()

}

