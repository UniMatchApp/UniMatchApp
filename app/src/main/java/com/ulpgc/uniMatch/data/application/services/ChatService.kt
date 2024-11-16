package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message

interface ChatService {
    suspend fun sendMessage(
        loggedUserId: String,
        chatId: String,
        content: String,
        attachment: String?
    ): Result<Message>

    suspend fun getChats(loggedUserId: String): Result<List<Chat>>

    suspend fun getMessages(chatId: String): Result<List<Message>>

    suspend fun getChatsByName(chatName: String): Result<List<Chat>>
}