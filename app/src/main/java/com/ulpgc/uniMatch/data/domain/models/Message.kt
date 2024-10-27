package com.ulpgc.uniMatch.data.domain.models

import com.ulpgc.uniMatch.data.infrastructure.entities.MessageEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageStatus

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
}