package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.application.services.UserService
import com.ulpgc.uniMatch.data.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val userViewModel: UserViewModel,
    private val matchingService: MatchingService,
    private val userService: UserService
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _matchingProfiles = MutableStateFlow(emptyList<Profile>())
    val matchingProfiles: StateFlow<List<Profile>> get() = _matchingProfiles

    fun loadMatchingUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _matchingProfiles.value = emptyList()
            val result = matchingService.getMatchingUsers(10)
            result.onSuccess { profiles ->
                Log.i("HomeViewModel", "Loaded matching users: $profiles")
                _matchingProfiles.value = profiles
                _isLoading.value = false
            }.onFailure {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreMatchingUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = matchingService.getMatchingUsers(10)
            result.onSuccess { profiles ->
                _matchingProfiles.value += profiles
                _isLoading.value = false
            }.onFailure {
                _isLoading.value = false
            }
        }
    }

    fun dislikeUser(targetId: String) {
        viewModelScope.launch {
            matchingService.dislikeUser(targetId).onSuccess {
                _matchingProfiles.value = _matchingProfiles.value.filter { it.userId != targetId }
            }
        }
    }

    fun likeUser(targetId: String) {
        viewModelScope.launch {
            matchingService.likeUser(targetId).onSuccess {
                _matchingProfiles.value = _matchingProfiles.value.filter { it.userId != targetId }
            }
        }
    }

    fun reportUser(reportedUserId: String, reason: String, details: String, extra: String = "") {
        viewModelScope.launch {
            val predefineReason = "$reason: $details"
            userService.reportUser(reportedUserId, predefineReason, extra).onSuccess {
                _matchingProfiles.value = matchingProfiles.value.filter { it.userId != reportedUserId }
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error reporting user"
                )
            }
        }
    }

    fun blockUser(blockedUserId: String) {
        viewModelScope.launch {
            userViewModel.userId?.let {
                userService.blockUser(blockedUserId).onSuccess {
                    _matchingProfiles.value = _matchingProfiles.value.filter { it.userId != blockedUserId }
                    Log.i("HomeViewModel", "User $blockedUserId has been blocked and profiles updated.")
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error blocking user"
                    )
                }
            }
        }
    }

}
