package com.ulpgc.uniMatch.data.infrastructure.viewModels

import MessageNotificationPayload
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.events.Event
import com.ulpgc.uniMatch.data.application.events.EventBus
import com.ulpgc.uniMatch.data.application.events.EventListener
import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.MessageStatus
import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.infrastructure.events.MessageNotificationEvent
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
) : ViewModel(), EventListener {

    private val customScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        customScope.launch {
            webSocketEventBus.subscribeToEvents(this@ChatViewModel)
        }
    }

    private val _searchQuery = MutableStateFlow("Buscar chats")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _chatList = MutableStateFlow<List<Chat>>(emptyList())
    val chatList: StateFlow<List<Chat>> get() = _chatList

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _otherUser = MutableStateFlow<Profile?>(null)
    val otherUser: StateFlow<Profile?> get() = _otherUser

    override suspend fun onEventReceived(event: Event) {
        when (event) {
            is MessageNotificationEvent -> {
                handleNewMessage(event.notification)
            }
        }
    }

    private fun handleNewMessage(notification: Notifications) {
        val message = notification.payload as MessageNotificationPayload
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            val newMessage = Message(
                messageId = message.id,
                senderId = message.getSender(),
                attachment = message.getThumbnail(),
                content = message.getContent(),
                timestamp = notification.date,
                recipientId = userViewModel.userId!!,
                chatId = message.getSender()
            )

            chatService.saveMessage(newMessage)

            chatService.setMessageStatus(userViewModel.userId!!, message.id, MessageStatus.RECEIVED)

            _messages.value += newMessage
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
                _isLoading.value = false
            }.onFailure { error ->
                Log.e("ChatViewModel", "Error loading chats: ${error.message}")
                errorViewModel.showError(
                    error.message ?: "Error loading chats"
                )
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

            // Set all messages as received if the recipient is the logged user
            messages.indices.forEach { index ->
                val message = messages[index]
                if (message.recipientId != userViewModel.userId ||
                    message.status == MessageStatus.READ
                ) return@forEach
                val updatedMessage = chatService.setMessageStatus(
                    userViewModel.userId!!,
                    message.messageId,
                    MessageStatus.READ
                ).getOrElse { return@forEach }
                messages[index] = updatedMessage
            }


            this@ChatViewModel._messages.value = messages.toMutableList()
            _isLoading.value = false
        }
    }


    fun sendMessage(chatId: String, content: String, attachment: String?) {
        viewModelScope.launch {
            // Throw error if the authViewModel.userId is null
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            val result =
                chatService.sendMessage(userViewModel.userId!!, chatId, content, attachment)
            result.onFailure { error ->
                Log.e("ChatViewModel", "Error sending message: ${error.message}")
            }
            _messages.value += result.getOrNull()!!
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

    fun setMessageStatus(messageId: String, status: MessageStatus) {
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
                userViewModel.userId!!, messageId, DeletedMessageStatus.DELETED_BY_RECIPIENT
            )

            result.onFailure { error ->
                Log.e("ChatViewModel", "Error deleting message: ${error.message}")
            }
        }
    }
}