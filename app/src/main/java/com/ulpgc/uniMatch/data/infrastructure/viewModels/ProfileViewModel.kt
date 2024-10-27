package com.ulpgc.uniMatch.data.infrastructure.viewModels
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.domain.models.ChatPreviewData
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageStatus
import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ProfileViewModel(
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _profileData = MutableStateFlow<Profile?>(null)
    val profileData: StateFlow<Profile?> get() = _profileData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.getProfile(authViewModel.userId!!)
            result.onSuccess { profileData ->
                _profileData.value = profileData
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error loading profile"
                )
                _isLoading.value = false
            }
        }
    }
}
