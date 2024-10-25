package com.ulpgc.uniMatch.data.infrastructure.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ErrorViewModel : ViewModel() {

    private val _errorState = MutableStateFlow<ErrorState?>(ErrorState.NoError)
    val errorState: StateFlow<ErrorState?> get() = _errorState

    fun showError(message: String) {
        viewModelScope.launch {
            _errorState.value = ErrorState.Error(message)
        }
    }

    fun clearError() {
        viewModelScope.launch {
            _errorState.value = ErrorState.NoError
        }
    }
}

sealed class ErrorState {
    object NoError : ErrorState()
    data class Error(val message: String) : ErrorState()
}
