package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.enums.MessageStatusType
import com.ulpgc.uniMatch.data.domain.models.Message


object MessageMock {

    fun createMockMessages(amount: Int = 10): List<Message> {
        val mockSenderIds = arrayOf("mock_user_id", "user_id_1", "user_id_2")
        val mockStatuses = arrayOf(
            MessageStatusType.SENDING,
            MessageStatusType.SENT,
            MessageStatusType.RECEIVED,
            MessageStatusType.READ,
            MessageStatusType.SENDING,
            MessageStatusType.FAILED

        )

        return List(amount) {
            Message(
                messageId = "message_$it",
                chatId = "chat_1",
                recipientId = mockSenderIds.random(),
                senderId = mockSenderIds.random(),
                content = "Message $it",
                timestamp = System.currentTimeMillis(),
                status = mockStatuses.random()
            )
        }
    }
}
