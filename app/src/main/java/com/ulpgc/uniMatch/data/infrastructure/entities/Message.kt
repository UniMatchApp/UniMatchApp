package com.ulpgc.uniMatch.data.infrastructure.entities

import com.ulpgc.uniMatch.data.infrastructure.entities.db.MessageEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.db.MessageStatus

data class Message(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
) {
    fun toEntity(): MessageEntity {
        return MessageEntity(
            messageId = messageId,
            chatId = chatId,
            senderId = senderId,
            content = content,
            timestamp = timestamp,
            status = status
        )
    }

    companion object {
        fun fromEntity(entity: MessageEntity): Message {
            return Message(
                messageId = entity.messageId,
                chatId = entity.chatId,
                senderId = entity.senderId,
                content = entity.content,
                timestamp = entity.timestamp,
                status = entity.status
            )
        }

        fun createMockMessages(amount: Int = 10): List<Message> {
            return List(amount) {
                Message(
                    messageId = "message_$it",
                    chatId = "chat_1",
                    senderId = arrayOf("mock_user_id", "user_id_1", "user_id_2").random(),
                    content = "Message $it",
                    timestamp = System.currentTimeMillis(),
                    status = arrayOf(MessageStatus.SENDING, MessageStatus.SENT, MessageStatus.READ, MessageStatus.RECEIVED, MessageStatus.FAILED).random()

                )
            }

        }
    }
}