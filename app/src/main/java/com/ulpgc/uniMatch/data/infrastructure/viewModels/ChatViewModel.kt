package com.ulpgc.uniMatch.data.infrastructure.viewModels

import MessageNotificationPayload
import UserStatusSocket
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.application.events.EventListener
import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.ChatStatus
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.models.notification.Notification

import com.ulpgc.uniMatch.data.infrastructure.events.GetUserStatusEvent
import com.ulpgc.uniMatch.data.infrastructure.events.MessageNotificationEvent
import com.ulpgc.uniMatch.data.infrastructure.events.StoppedTypingEvent
import com.ulpgc.uniMatch.data.infrastructure.events.TypingEvent
import com.ulpgc.uniMatch.data.infrastructure.events.UserOfflineEvent
import com.ulpgc.uniMatch.data.infrastructure.events.UserOnlineEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


open class ChatViewModel(
    private val chatService: ChatService,
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val userViewModel: UserViewModel,
    private val webSocketEventBus: EventBus,
    private val statusSocket: UserStatusSocket
) : ViewModel(), EventListener {

    private val customScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        customScope.launch {
            webSocketEventBus.subscribeToEvents(this@ChatViewModel)
        }
    }

    private val _searchQuery = MutableStateFlow("Buscar chats")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _chatList = MutableLiveData<List<Chat>>(emptyList())
    val chatList: LiveData<List<Chat>> get() = _chatList

    private val _usersStatus = MutableStateFlow<Map<String, ChatStatus>>(emptyMap())
    val usersStatus: StateFlow<Map<String, ChatStatus>> get() = _usersStatus

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> get() = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _otherUser = MutableStateFlow<Profile?>(null)
    val otherUser: StateFlow<Profile?> get() = _otherUser

    override suspend fun onEventReceived(event: Event) {
        when (event) {
            is MessageNotificationEvent -> {
                handleNewMessage(event.notification)
            }
            is UserOnlineEvent -> {
                handleUserOnline(event.userId)
            }
            is UserOfflineEvent -> {
                handleUserOffline(event.userId)
            }
            is TypingEvent -> {
                handleTyping(event.userId, event.targetUserId)
            }
            is StoppedTypingEvent -> {
                handleStoppedTyping(event.userId)
            }
            is GetUserStatusEvent -> {
                handleGetUserStatus(event.userId, event.status)
            }
        }
    }

    private fun handleNewMessage(notification: Notification) {
        val message = notification.payload as MessageNotificationPayload
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }

            Log.i("ChatViewModel", "New message received: $message")

            val newMessage = Message(
                messageId = notification.contentId,
                senderId = message.getSender(),
                attachment = message.getThumbnail(),
                content = message.getContent(),
                createdAt = notification.date,
                recipientId = message.getRecipient(),
                receptionStatus = message.getReceptionStatus(),
                contentStatus = message.getContentStatus(),
                deletedStatus = message.getDeletedStatus()
            )

            chatService.saveMessage(newMessage, userViewModel.userId!!)

            if (newMessage.senderId != userViewModel.userId && newMessage.receptionStatus != ReceptionStatus.RECEIVED) {
                setMessagesAsReceived(listOf(newMessage))
            }

            _messages.value?.find { it.messageId == newMessage.messageId }?.let {
                _messages.value = _messages.value!!.toMutableList().apply {
                    this[indexOf(it)] = newMessage
                }
            } ?: run {
                _messages.value = _messages.value?.plus(newMessage)
            }

            _chatList.value?.find { it.userId == newMessage.senderId }?.let {
                _chatList.value = _chatList.value?.toMutableList().apply {
                    this?.set(indexOf(it),
                        it.copy(lastMessage = newMessage, unreadMessagesCount = it.unreadMessagesCount + 1)
                    )
                }
            }
        }
    }

    private fun handleUserOnline(userId: String) {
        if (_usersStatus.value.containsKey(userId)) {
            _usersStatus.value = _usersStatus.value.toMutableMap().apply {
                this[userId] = ChatStatus.ONLINE
            }
        }
    }

    private fun handleUserOffline(userId: String) {
        if (_usersStatus.value.containsKey(userId)) {
            _usersStatus.value = _usersStatus.value.toMutableMap().apply {
                this[userId] = ChatStatus.OFFLINE
            }
        }
    }

    private fun handleTyping(userId: String, targetUserId: String) {
        if (userId == userViewModel.userId) {
            if (_usersStatus.value.containsKey(targetUserId)) {
                Log.i("ChatViewModel", "User is typing")
                _usersStatus.value = _usersStatus.value.toMutableMap().apply {
                    this[targetUserId] = ChatStatus.TYPING
                }
            }
        }
    }

    private fun handleStoppedTyping(userId: String) {
        if (_usersStatus.value.containsKey(userId)) {
            _usersStatus.value = _usersStatus.value.toMutableMap().apply {
                this[userId] = ChatStatus.ONLINE
            }
        }
    }

    private fun handleGetUserStatus(userId: String, status: String) {
        if (_usersStatus.value.containsKey(userId)) {
            _usersStatus.value = _usersStatus.value.toMutableMap().apply {
                this[userId] = ChatStatus.valueOf(status)
            }
        }
    }

    fun loadChats() {
        Log.i("ChatViewModel", "Loading chats")
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            _isLoading.value = true
            val result = chatService.getChats(userViewModel.userId!!)
            result.onSuccess { chats ->
                _chatList.value = chats
                _usersStatus.value = chats.associate { it.userId to ChatStatus.OFFLINE }
                chats.forEach { chat ->
                    if (chat.userId != userViewModel.userId) {
                        statusSocket.getUserStatus(userViewModel.userId!!, chat.userId)
                    }
                }
                _isLoading.value = false
            }.onFailure { error ->
                Log.e("ChatViewModel", "Error loading chats: ${error.message}")
                _isLoading.value = false
            }
        }
    }

    fun loadMessages(chatId: String, offset: Int, limit: Int = 100) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            _isLoading.value = true

            // Obtener el perfil del otro usuario y manejar el Result
            val otherUserResult = profileService.getProfile(chatId)
            otherUserResult.onSuccess { profile ->
                _otherUser.value = profile
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error loading other user profile"
                )
            }

            val messages = (chatService.getMessages(chatId, offset, limit)
                .onFailure {
                    errorViewModel.showError(it.message ?: "Error loading messages")
                    _isLoading.value = false
                }.getOrDefault(emptyList())).toMutableList()


            this@ChatViewModel._messages.value = (this@ChatViewModel._messages.value?.plus(messages))?.distinctBy { it.messageId }
                ?.toMutableList()
            _isLoading.value = false
        }
    }


    fun sendMessage(chatId: String, content: String, attachment: String?) {
        viewModelScope.launch {

            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }

            val result = chatService.sendMessage(userViewModel.userId!!, chatId, content, attachment)

            result.onFailure { error ->
                Log.e("ChatViewModel", "Error sending message: ${error.message}")
            }

            result.getOrNull()?.let { message ->
                _messages.value = _messages.value?.plus(message)
            } ?: run {
                Log.e("ChatViewModel", "Failed to send message: Result is null")
            }
        }
    }


    fun filterChats(recipientName: String) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            _isLoading.value = true
            val result = chatService.getChatsByName(userViewModel.userId!!, recipientName)
            result.onSuccess { chats ->
                _chatList.value = chats
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error searching for chats"
                )
                _isLoading.value = false
            }
        }
    }

    fun setUserTyping(targetUserId: String) {
        customScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            statusSocket.setUserTyping(userViewModel.userId!!, targetUserId)
        }
    }

    fun setUserStoppedTyping() {
        customScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            statusSocket.setUserStoppedTyping(userViewModel.userId!!)
        }
    }

    fun setMessageStatus(messageId: String, status: ReceptionStatus) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            val result: Result<Message> =
                chatService.setMessageStatus(userViewModel.userId!!, messageId, status)
            result.onSuccess {

            }
            result.onFailure { error ->
                Log.e("ChatViewModel", "Error setting message status: ${error.message}")
            }
        }
    }

    fun setMessagesAsRead(messages: List<Message>) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }

            messages.forEach { message ->
                val result = chatService.messageHasBeenRead(message, userViewModel.userId!!)

                result.onFailure { error ->
                    Log.e("ChatViewModel", "Error setting message status: ${error.message}")
                }
            }
        }
    }

    fun setMessagesAsReceived(messages: List<Message>) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }

            messages.forEach { message ->
                val result = chatService.messageHasBeenReceived(message, userViewModel.userId!!)

                result.onFailure { error ->
                    Log.e("ChatViewModel", "Error setting message status: ${error.message}")
                }
            }

        }
    }


    fun editMessage(messageId: String, newContent: String) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }

            val result = chatService.editMessageContent(
                userViewModel.userId!!, messageId, newContent
            )

            result.onFailure { error ->
                Log.e("ChatViewModel", "Error editing message: ${error.message}")
            }
        }
    }

    fun deleteMessageAsSender(messageId: String, forAll: Boolean = false) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }

            val result = chatService.deleteMessage(
                userViewModel.userId!!,
                messageId,
                if (forAll) DeletedMessageStatus.DELETED_FOR_BOTH else DeletedMessageStatus.DELETED_BY_SENDER
            )

            result.onFailure { error ->
                Log.e("ChatViewModel", "Error deleting message: ${error.message}")
            }
        }
    }

    fun deleteMessageAsRecipient(messageId: String) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }

            val result = chatService.deleteMessage(
                userViewModel.userId!!, messageId, DeletedMessageStatus.NOT_DELETED
            )

            result.onFailure { error ->
                Log.e("ChatViewModel", "Error deleting message: ${error.message}")
            }
        }
    }
}