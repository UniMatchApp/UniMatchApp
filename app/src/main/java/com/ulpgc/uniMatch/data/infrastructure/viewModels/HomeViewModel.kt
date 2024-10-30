package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val authViewModel: AuthViewModel,
    private val matchingService: MatchingService
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _matchingProfiles = MutableStateFlow(emptyList<Profile>())
    val matchingProfiles: StateFlow<List<Profile>> get() = _matchingProfiles

    fun loadMatchingUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authViewModel.userId?.let { matchingService.getMatchingUsers(it, 10) }
            if (result != null) {
                result.onSuccess { profiles ->
                    _matchingProfiles.value = profiles
                    _isLoading.value = false
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error loading matching users"
                    )
                    _isLoading.value = false
                }
            }
        }
    }

    fun loadMoreMatchingUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authViewModel.userId?.let { matchingService.getMatchingUsers(it, 10) }
            if (result != null) {
                result.onSuccess { profiles ->
                    _matchingProfiles.value = _matchingProfiles.value + profiles
                    _isLoading.value = false
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error loading matching users"
                    )
                    _isLoading.value = false
                }
            }
        }
    }

    fun dislikeUser(userId: String, targetId: String) {
        viewModelScope.launch {
            authViewModel.userId?.let { userId ->
                matchingService.dislikeUser(userId, targetId)?.onSuccess {
                    // Eliminar el perfil de la lista
                    Log.i("Matching Profiles size", _matchingProfiles.value.size.toString())
                    _matchingProfiles.value = _matchingProfiles.value.filter { it.userId != targetId }
                    Log.i("Matching Profiles size", _matchingProfiles.value.size.toString())
                }?.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error disliking user"
                    )
                }
            }
        }
    }

    fun likeUser(userId: String, targetId: String) {
        viewModelScope.launch {
            authViewModel.userId?.let { userId ->
                matchingService.likeUser(userId, targetId)?.onSuccess {
                    // Eliminar el perfil de la lista
                    _matchingProfiles.value = _matchingProfiles.value.filter { it.userId != targetId }
                }?.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error liking user"
                    )
                }
            }
        }
    }

}
