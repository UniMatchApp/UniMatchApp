package com.ulpgc.uniMatch.data.infrastructure.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel (
    private val notificationsService: NotificationsService,
    private val errorViewModel: ErrorViewModel,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _notifications = MutableStateFlow<List<Notifications>>(emptyList())
    val notifications: StateFlow<List<Notifications>> get() = _notifications

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> get() = _notificationsEnabled

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userViewModel.userId?.let { notificationsService.getNotifications(it) }
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
            result.onSuccess {
                _notifications.value = _notifications.value.map { notification ->
                    if (notification.id == notificationId) {
                        notification.copy(status = NotificationStatus.READ)
                    } else {
                        notification
                    }
                }
            }
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
            result.onSuccess {
                _notifications.value = _notifications.value.filter { notification ->
                    notification.id != notificationId
                }
            }
            result.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error deleting notification"
                )
            }
        }
    }

    fun deleteAllNotifications() {
        viewModelScope.launch {
            userViewModel.userId?.let { notificationsService.deleteAllNotifications(it) }
                ?.onSuccess {
                    _notifications.value = emptyList()
                }
                ?.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error deleting all notifications"
                    )
                }
        }
    }

    fun disableAllNotifications() {
        TODO("Not yet implemented")
    }

    fun toggleNotifications(it: Boolean) {
        _notificationsEnabled.value = it
    }
}