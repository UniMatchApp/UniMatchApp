package com.ulpgc.uniMatch.data.infrastructure.services.chat

import com.ulpgc.uniMatch.data.infrastructure.entities.ChatPreviewData
import com.ulpgc.uniMatch.data.infrastructure.entities.Message

class MockChatService: ChatService {
    override suspend fun getChats(): Result<List<ChatPreviewData>> {
        return Result.success(
             ChatPreviewData.createMockChatPreviews()
        )
    }

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        return Result.success(
            Message.createMockMessages()
        )
    }
}