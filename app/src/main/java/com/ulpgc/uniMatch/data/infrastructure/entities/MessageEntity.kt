package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.domain.enums.ContentStatus
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
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
    val receptionStatus: ReceptionStatus,
    val contentStatus: ContentStatus,
    val deletedStatus: DeletedMessageStatus,
    val attachment: String? = null
) {
    companion object {

        fun fromDomain(message: Message, loggedUserId: String): MessageEntity {
            // Determina el otro participante del chat
            val chatId = if (message.senderId == loggedUserId) {
                message.recipientId
            } else {
                message.senderId
            }

            return MessageEntity(
                messageId = message.messageId,
                chatId = chatId,
                content = message.content,
                senderId = message.senderId,
                recipientId = message.recipientId,
                timestamp = message.createdAt,
                receptionStatus = message.receptionStatus,
                contentStatus = message.contentStatus,
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
                createdAt = messageEntity.timestamp,
                receptionStatus = messageEntity.receptionStatus,
                contentStatus = messageEntity.contentStatus,
                deletedStatus = messageEntity.deletedStatus,
                attachment = messageEntity.attachment
            )
        }
    }
}
