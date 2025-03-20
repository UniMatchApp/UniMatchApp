package com.ulpgc.uniMatch.data.infrastructure.services.chat

import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.domain.enums.ContentStatus
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
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
                content,
                loggedUserId,
                chatId,
                attachment,
                ReceptionStatus.SENDING
            )
        )
    }

    override suspend fun messageHasBeenRead(message: Message, loggedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getChats(loggedUserId: String): Result<List<Chat>> {
        val chats = emptyList<Chat>()

        return Result.success(
            ChatPreviewDataMock.createChatPreviewDataMocks()
        )
    }

    override suspend fun getChat(loggedUserId: String, chatId: String): Result<Chat?> {
        return Result.success(
            ChatPreviewDataMock.createChatPreviewDataMocks().firstOrNull()
        )
    }

    override suspend fun getMessages(
        chatId: String,
        offset: Int,
        limit: Int
    ): Result<List<Message>> {
        return Result.success(
            MessageMock.createMockMessages(10)
        )
    }

    override suspend fun getLatestMessage(chatId: String): Result<Message> {
        return Result.success(
            MessageMock.createMockMessages(1).first()
        )
    }

    override suspend fun messageHasBeenReceived(message: Message, loggedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getChatsByName(loggedUserId: String, filterName: String): Result<List<Chat>> {
        return Result.success(
            ChatPreviewDataMock.searchChatPreviewDataMocks(filterName)
        )
    }

    override suspend fun saveMessage(message: Message, loggedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun setMessageStatus(
        loggedUserId: String,
        messageId: String,
        status: ReceptionStatus
    ): Result<Message> {
        return Result.success(
            Message(
                messageId,
                "content",
                loggedUserId,
                UUID.randomUUID().toString(),
                null,
                status
            )
        )
    }

    override suspend fun editMessageContent(
        userId: String,
        messageId: String,
        newContent: String
    ): Result<Message> {
        return Result.success(
            Message(
                messageId,
                newContent,
                userId,
                UUID.randomUUID().toString(),
                null,
                ReceptionStatus.READ
            )
        )
    }

    override suspend fun deleteMessage(
        userId: String,
        messageId: String,
        deletedStatus : DeletedMessageStatus
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteChat(userId: String, chatId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteLocalMessage(messageId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateMessageAttachment(messageId: String, attachment: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun messageExistsLocal(messageId: String): Result<Boolean> {
        return Result.success(true)
    }
}