package com.ulpgc.uniMatch.data.infrastructure.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel (
    private val notificationsService: NotificationsService,
    private val errorViewModel: ErrorViewModel,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _notifications = MutableStateFlow<List<Notifications>>(emptyList())
    val notifications: StateFlow<List<Notifications>> get() = _notifications

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authViewModel.userId?.let { notificationsService.getNotifications(it) }
            if (result != null) {
                result.onSuccess { notifications ->
                    _notifications.value = notifications
                    _isLoading.value = false
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error loading notifications"
                    )
                    _isLoading.value = false
                }
            }
        }
    }

    fun markNotificationAsRead(notificationId: String) {
        viewModelScope.launch {
            val result = notificationsService.markNotificationAsRead(notificationId)
            result.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error marking notification as read"
                )
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            val result = notificationsService.deleteNotification(notificationId)
            result.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error deleting notification"
                )
            }
        }
    }

    fun deleteAllNotifications() {
        viewModelScope.launch {
            authViewModel.userId?.let { notificationsService.deleteAllNotifications(it) }
                ?.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error deleting all notifications"
                    )
                }
        }
    }
}