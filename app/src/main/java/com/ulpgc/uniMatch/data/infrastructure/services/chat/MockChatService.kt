package com.ulpgc.uniMatch.data.infrastructure.services.chat

import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.domain.models.ChatPreviewData
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.mocks.ChatPreviewDataMock
import com.ulpgc.uniMatch.data.infrastructure.mocks.MessageMock
import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock

class MockChatService: ChatService {
    override suspend fun getChats(): Result<List<ChatPreviewData>> {
        return Result.success(
             ChatPreviewDataMock.createChatPreviewDataMocks()
        )
    }

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        return Result.success(
            MessageMock.createMockMessages(10)
        )
    }

    override suspend fun getOtherUserByChatId(chatId: String): Result<Profile> {
        return Result.success(
            ProfileMock.createMockProfile()
        )
    }
}