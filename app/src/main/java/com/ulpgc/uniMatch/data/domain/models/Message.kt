package com.ulpgc.uniMatch.data.domain.models

import com.google.gson.annotations.SerializedName
import com.ulpgc.uniMatch.data.domain.enums.ContentStatus
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import java.util.Date
import java.util.UUID


data class Message(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("content") var content: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("recipientId") val recipientId: String,
    @SerializedName("attachment") var attachment: String? = null,
    @SerializedName("receptionStatus") var receptionStatus: ReceptionStatus,
    @SerializedName("contentStatus") var contentStatus: ContentStatus = ContentStatus.NOT_EDITED,
    @SerializedName("deletedStatus") var deletedStatus: DeletedMessageStatus = DeletedMessageStatus.NOT_DELETED,
    @SerializedName("createdAt") var createdAt: Long = Date().time,
    @SerializedName("updatedAt") var updatedAt: Long = Date().time
) {
    companion object {
        fun createMessage(
            content: String,
            chatId: String,
            senderId: String,
            recipientId: String
        ): Message {
            return Message(
                messageId = UUID.randomUUID().toString(),
                content = content,
                senderId = senderId,
                recipientId = recipientId,
                receptionStatus = ReceptionStatus.SENDING
            )
        }
    }

}

data class ModifyMessageDTO(
    @SerializedName("content") var content: String? = null,
    @SerializedName("status") var status: ReceptionStatus? = null,
    @SerializedName("deletedStatus") var deletedStatus: DeletedMessageStatus = DeletedMessageStatus.NOT_DELETED
) {
    companion object {
        fun create(
            content: String? = null,
            status: ReceptionStatus? = null,
            deletedStatus: DeletedMessageStatus = DeletedMessageStatus.NOT_DELETED
        ): ModifyMessageDTO {
            return ModifyMessageDTO(
                content = content,
                status = status,
                deletedStatus = deletedStatus
            )
        }
    }
}
