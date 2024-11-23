package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.MessageStatus
import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message

interface ChatService {
    suspend fun sendMessage(
        loggedUserId: String, chatId: String, content: String, attachment: String?
    ): Result<Message>

    suspend fun saveMessage(message: Message): Result<Unit>

    suspend fun getChats(loggedUserId: String): Result<List<Chat>>

    suspend fun getMessages(chatId: String, offset: Int, limit: Int): Result<List<Message>>

    suspend fun getChatsByName(loggedUserId: String, filterName: String): Result<List<Chat>>

    suspend fun setMessageStatus(
        loggedUserId: String, messageId: String, status: MessageStatus
    ): Result<Message>

    suspend fun editMessageContent(
        userId: String,
        messageId: String,
        newContent: String
    ): Result<Message>

    suspend fun deleteMessage(
        userId: String,
        messageId: String,
        deletedStatus: DeletedMessageStatus
    ): Result<Message>
}