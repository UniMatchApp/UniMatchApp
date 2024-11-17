package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.enums.MessageStatus
import com.ulpgc.uniMatch.data.domain.models.Message


object MessageMock {

    fun createMockMessages(amount: Int = 10): List<Message> {
        val mockSenderIds = arrayOf("mock_user_id", "user_id_1", "user_id_2")
        val mockStatuses = arrayOf(
            MessageStatus.SENDING,
            MessageStatus.SENT,
            MessageStatus.RECEIVED,
            MessageStatus.READ,
            MessageStatus.SENDING,
            MessageStatus.FAILED

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
