package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.MessageStatus
import com.ulpgc.uniMatch.data.domain.models.Message

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chatId"])]
)
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val chatId: String,
    val content: String,
    val senderId: String,
    val recipientId: String,
    val timestamp: Long,
    val status: MessageStatus,
    val deletedStatus: DeletedMessageStatus,
    val attachment: String? = null
) {
    companion object {
        fun fromDomain(message: Message): MessageEntity {
            return MessageEntity(
                messageId = message.messageId,
                chatId = message.senderId,
                content = message.content,
                senderId = message.senderId,
                recipientId = message.recipientId,
                timestamp = message.timestamp,
                status = message.status,
                deletedStatus = message.deletedStatus,
                attachment = message.attachment
            )
        }

        fun toDomain(messageEntity: MessageEntity): Message {
            return Message(
                messageId = messageEntity.messageId,
                content = messageEntity.content,
                senderId = messageEntity.senderId,
                recipientId = messageEntity.recipientId,
                timestamp = messageEntity.timestamp,
                status = messageEntity.status,
                deletedStatus = messageEntity.deletedStatus,
                attachment = messageEntity.attachment
            )
        }
    }
}

