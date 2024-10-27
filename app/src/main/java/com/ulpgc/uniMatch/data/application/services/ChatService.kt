package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.ChatPreviewData
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.Profile

interface ChatService {
    suspend fun getChats(): Result<List<ChatPreviewData>>

    suspend fun getMessages(chatId: String): Result<List<Message>>

    suspend fun getOtherUserByChatId(chatId: String): Result<Profile>
}