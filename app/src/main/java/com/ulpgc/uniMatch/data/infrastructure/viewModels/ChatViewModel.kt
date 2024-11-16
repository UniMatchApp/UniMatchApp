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
import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.models.notification.Notifications
import com.ulpgc.uniMatch.data.infrastructure.events.MessageNotificationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


open class ChatViewModel(
    private val chatService: ChatService,
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val userViewModel: UserViewModel,
    private val webSocketEventBus: EventBus
) : ViewModel(), EventListener {

    init {
        viewModelScope.launch {
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

    private fun handleNewMessage(notification: Notifications)  {
        val message = notification.payload as MessageNotificationPayload

        val messageId = message.id
        val senderId = message.getSender()
        val attachment = message.getThumbnail()
        val content = message.getContent()
        val timestamp = notification.date

        val newMessage = Message(
            messageId = messageId,
            senderId = senderId,
            attachment = attachment,
            content = content,
            timestamp = timestamp,
            recipientId = userViewModel.userId!!,
            chatId = senderId
        )
        _messages.value += newMessage
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
                errorViewModel.showError(
                    error.message ?: "Error loading chats"
                )
                _isLoading.value = false
            }
        }
    }

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            if (userViewModel.userId.isNullOrEmpty()) {
                errorViewModel.showError("User is not authenticated")
                return@launch
            }
            _isLoading.value = true
            val result = chatService.getMessages(chatId)
            result.onSuccess { messages ->
                _messages.value = messages
                _isLoading.value = false
                // Obtener el perfil del otro usuario y manejar el Result
                val otherUserResult = profileService.getProfile(chatId)
                otherUserResult.onSuccess { profile ->
                    _otherUser.value = profile
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error loading other user profile"
                    )
                }
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error loading messages"
                )
                _isLoading.value = false
            }
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
            _isLoading.value = true
            val result = chatService.getChatsByName(recipientName)
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


    fun editMessage(messageId: String, newContent: String) {
        viewModelScope.launch {
            // Lógica para editar el mensaje (actualizar la base de datos local y notificar cambios al backend)
        }

    }

    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            // Lógica para eliminar el mensaje (actualizar la base de datos local y notificar cambios al backend)
        }
    }

}