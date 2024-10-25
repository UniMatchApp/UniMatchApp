package com.ulpgc.uniMatch.data.infrastructure.services.chat

import com.ulpgc.uniMatch.data.infrastructure.entities.ChatPreviewData
import com.ulpgc.uniMatch.data.infrastructure.entities.Message

interface ChatService {
    suspend fun getChats(): Result<List<ChatPreviewData>>

    suspend fun getMessages(chatId: String): Result<List<Message>>
}