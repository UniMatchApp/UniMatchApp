package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageStatus {
    SENDING,
    SENT,
    READ,
    RECEIVED,
    FAILED
}

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
)

