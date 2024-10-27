package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageStatus

object MessageMock {

    fun createMockMessages(amount: Int = 10): List<Message> {
        val mockSenderIds = arrayOf("mock_user_id", "user_id_1", "user_id_2")
        val mockStatuses = arrayOf(
            MessageStatus.SENDING,
            MessageStatus.SENT,
            MessageStatus.READ,
            MessageStatus.RECEIVED,
            MessageStatus.FAILED
        )

        return List(amount) {
            Message(
                messageId = "message_$it",
                chatId = "chat_1",
                senderId = mockSenderIds.random(),
                content = "Message $it",
                timestamp = System.currentTimeMillis(),
                status = mockStatuses.random()
            )
        }
    }
}
