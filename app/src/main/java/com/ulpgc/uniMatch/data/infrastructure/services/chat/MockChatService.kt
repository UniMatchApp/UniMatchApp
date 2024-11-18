package com.ulpgc.uniMatch.data.infrastructure.services.chat

import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.infrastructure.mocks.ChatPreviewDataMock
import com.ulpgc.uniMatch.data.infrastructure.mocks.MessageMock
import java.util.UUID

class MockChatService : ChatService {
    override suspend fun sendMessage(
        loggedUserId: String,
        chatId: String,
        content: String,
        attachment: String?
    ): Result<Message> {
        return Result.success(
            Message(
                UUID.randomUUID().toString(),
                chatId,
                content,
                loggedUserId,
                chatId,
                attachment,
            )
        )
    }

    override suspend fun getChats(loggedUserId: String): Result<List<Chat>> {
        val chats = emptyList<Chat>()

        return Result.success(
            ChatPreviewDataMock.createChatPreviewDataMocks()
        )
    }

    override suspend fun getMessages(chatId: String, offset: Int, limit: Int): Result<List<Message>> {
        return Result.success(
            MessageMock.createMockMessages(10)
        )
    }

    override suspend fun getChatsByName(chatName: String): Result<List<Chat>> {
        return Result.success(
            ChatPreviewDataMock.searchChatPreviewDataMocks(chatName)
        )
    }
}