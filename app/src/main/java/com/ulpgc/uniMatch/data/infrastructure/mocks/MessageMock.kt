package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.models.Message


object MessageMock {

    fun createMockMessages(amount: Int = 10): List<Message> {
        val mockSenderIds = arrayOf("mock_user_id", "user_id_1", "user_id_2")
        val mockStatuses = arrayOf(
            ReceptionStatus.SENDING,
            ReceptionStatus.SENT,
            ReceptionStatus.RECEIVED,
            ReceptionStatus.READ,
            ReceptionStatus.SENDING,
            ReceptionStatus.FAILED

        )

        return List(amount) {
            Message(
                messageId = "message_$it",
                recipientId = mockSenderIds.random(),
                senderId = mockSenderIds.random(),
                content = "Message $it",
                timestamp = System.currentTimeMillis(),
                receptionStatus = mockStatuses.random()
            )
        }
    }
}
