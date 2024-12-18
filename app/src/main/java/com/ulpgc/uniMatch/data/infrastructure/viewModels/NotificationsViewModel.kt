package com.ulpgc.uniMatch.data.infrastructure.viewModels

import MessageNotificationPayload
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.application.events.EventListener
import com.ulpgc.uniMatch.data.application.services.NotificationsService
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.models.notification.Notification
import com.ulpgc.uniMatch.data.infrastructure.events.AppNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.EventNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.MatchNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.MessageNotificationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel (
    private val notificationsService: NotificationsService,
    private val errorViewModel: ErrorViewModel,
    private val webSocketEventBus: EventBus,
    private val userViewModel: UserViewModel
) : ViewModel(), EventListener {

    private val customScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        customScope.launch {
            webSocketEventBus.subscribeToEvents(this@NotificationsViewModel)
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> get() = _notifications

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> get() = _notificationsEnabled

    override suspend fun onEventReceived(event: Event) {
        when (event) {
            is AppNotificationEvent -> {
                handleAppNotification(event.notification)
            }
            is EventNotificationEvent -> {
                handleEventNotification(event.notification)
            }
            is MatchNotificationEvent -> {
                handleMatchNotification(event.notification)
            }
            is MessageNotificationEvent -> {
                handleMessageNotification(event.notification)
            }
        }
    }

    private fun handleAppNotification(notification: Notification) {
        _notifications.value = _notifications.value.toMutableList().apply {
            add(0, notification)
        }
    }

    private fun handleEventNotification(notification: Notification) {
        _notifications.value = _notifications.value.toMutableList().apply {
            add(0, notification)
        }
    }

    private fun handleMatchNotification(notification: Notification) {
        Log.d("NotificationsViewModel", "Notification received: $notification")
        _notifications.value = _notifications.value.toMutableList().apply {
            add(0, notification)
        }
    }

    private fun handleMessageNotification(notification: Notification) {
        if (notification.recipient != userViewModel.userId) {
            return
        }

        val payload = notification.payload as MessageNotificationPayload
        if (payload.getReceptionStatus() != ReceptionStatus.RECEIVED) {
            return
        }

        _notifications.value = _notifications.value.toMutableList().apply {
            add(0, notification)
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userViewModel.userId?.let { notificationsService.getNotifications(it) }
            if (result != null) {
                result.onSuccess { notifications ->
                    _notifications.value = notifications
                    _isLoading.value = false
                }.onFailure {
                    errorViewModel.showError(
                        it.message ?: "Error loading notifications"
                    )
                    _isLoading.value = false
                }
            }
        }
    }

    fun markNotificationAsRead(notificationId: String) {
        viewModelScope.launch {
            val notification = _notifications.value.find { it.id == notificationId }

            if (notification?.status == NotificationStatus.READ) {
                return@launch
            }

            val result = notificationsService.markNotificationAsRead(notificationId)
            result.onSuccess {
                notification?.let {
                    it.status = NotificationStatus.READ
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

    fun toggleNotifications(it: Boolean) {
        _notificationsEnabled.value = it
    }
}