package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatPreviewData
import com.ulpgc.uniMatch.data.infrastructure.entities.Message
import com.ulpgc.uniMatch.data.infrastructure.entities.db.MessageStatus
import com.ulpgc.uniMatch.data.infrastructure.services.auth.AuthService
import com.ulpgc.uniMatch.data.infrastructure.services.chat.ChatService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


open class ChatViewModel(
    private val chatService: ChatService,
    private val errorViewModel: ErrorViewModel,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _chatPreviewDataList = MutableStateFlow<List<ChatPreviewData>>(emptyList())
    val chatPreviewDataList: StateFlow<List<ChatPreviewData>> get() = _chatPreviewDataList

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadChats() {
        Log.i("ChatViewModel", "Loading chats")
        viewModelScope.launch {
            _isLoading.value = true
            val result = chatService.getChats()
            result.onSuccess { chats ->
                // Ordenar los chats por la fecha del último mensaje
                val sortedChats = chats.sortedByDescending { it.lastMessageTime }
                _chatPreviewDataList.value = sortedChats
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
            _isLoading.value = true
            val result = chatService.getMessages(chatId)
            result.onSuccess { messages ->
                _messages.value = messages
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error loading messages"
                )
                _isLoading.value = false
            }
        }
    }

    fun sendMessage(chatId: String, content: String) {
        viewModelScope.launch {
            val message = Message(
                messageId = generateMessageId(),
                chatId = chatId,
                senderId = authViewModel.userId ?: "",
                content = content,
                timestamp = System.currentTimeMillis(),
                status = MessageStatus.SENDING
            )
            _messages.value += message
            // TODO: Llamar al servicio para enviar el mensaje y luego actualizar el estado
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

    private fun generateMessageId(): String {
        // Genera un ID único para el mensaje
        return System.currentTimeMillis().toString()
    }
}