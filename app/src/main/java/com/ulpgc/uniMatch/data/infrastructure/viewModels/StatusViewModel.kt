package com.ulpgc.uniMatch.data.infrastructure.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.application.events.EventListener
import com.ulpgc.uniMatch.data.application.services.UserStatusService
import com.ulpgc.uniMatch.data.domain.enums.ChatStatusEnum
import com.ulpgc.uniMatch.data.infrastructure.events.GetUserStatusEvent
import com.ulpgc.uniMatch.data.infrastructure.events.StoppedTypingEvent
import com.ulpgc.uniMatch.data.infrastructure.events.UserOnlineEvent
import com.ulpgc.uniMatch.data.infrastructure.events.UserOfflineEvent
import com.ulpgc.uniMatch.data.infrastructure.events.TypingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserStatusViewModel(
    private val userStatusService: UserStatusService,
    private val errorViewModel: ErrorViewModel,
    private val webSocketEventBus: EventBus,
    private val userViewModel: UserViewModel
) : ViewModel(), EventListener {

    private val customScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        customScope.launch {
            webSocketEventBus.subscribeToEvents(this@UserStatusViewModel)
        }
    }

    private val _userStatuses = MutableStateFlow<Map<String, ChatStatusEnum>>(emptyMap())
    val userStatuses: StateFlow<Map<String, ChatStatusEnum>> get() = _userStatuses

    override suspend fun onEventReceived(event: Event) {
        when (event) {
            is UserOnlineEvent -> {
                handleUserOnline(event.userId)
            }
            is UserOfflineEvent -> {
                handleUserOffline(event.userId)
            }
            is TypingEvent -> {
                handleUserTyping(event.userId)
            }
            is StoppedTypingEvent -> {
                handleUserStoppedTyping(event.userId)
            }
            is GetUserStatusEvent -> {
                handleGetUserStatus(event.userId, event.status)
            }
        }
    }

    private fun handleUserOnline(userId: String) {
        if (_userStatuses.value.containsKey(userId)) {
            _userStatuses.value = _userStatuses.value.toMutableMap().apply {
                this[userId] = ChatStatusEnum.ONLINE
            }
        }
    }

    private fun handleUserOffline(userId: String) {
        if (_userStatuses.value.containsKey(userId)) {
            _userStatuses.value = _userStatuses.value.toMutableMap().apply {
                remove(userId)
            }
        }
    }

    private fun handleUserTyping(userId: String) {
        updateUserStatus(userId, ChatStatusEnum.TYPING)
    }

    private fun handleUserStoppedTyping(userId: String) {
        updateUserStatus(userId, ChatStatusEnum.ONLINE)
    }

    private fun handleGetUserStatus(userId: String, status: String) {
        val chatStatus = ChatStatusEnum.valueOf(status)
        updateUserStatus(userId, chatStatus)
    }

    private fun updateUserStatus(userId: String, chatStatus: ChatStatusEnum) {
        _userStatuses.value = _userStatuses.value.toMutableMap().apply {
            this[userId] = chatStatus
        }
    }

    fun setUserTyping(targetUserId: String) {
        viewModelScope.launch {
            userStatusService.setUserTyping(userViewModel.userId!!, targetUserId)
        }
    }

    fun setUserStoppedTyping() {
        viewModelScope.launch {
            userStatusService.setUserStoppedTyping(userViewModel.userId!!)
        }
    }

    fun getUserStatus(targetUserId: String) {
        viewModelScope.launch {
            userStatusService.getUserStatus(userViewModel.userId!!, targetUserId)
        }
    }
}
