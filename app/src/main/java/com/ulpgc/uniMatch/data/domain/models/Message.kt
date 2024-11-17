package com.ulpgc.uniMatch.data.domain.models

import com.google.gson.annotations.SerializedName
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatusType
import com.ulpgc.uniMatch.data.domain.enums.MessageStatusType
import java.util.Date
import java.util.UUID


data class Message(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("chatId") val chatId: String,
    @SerializedName("content") var content: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("recipientId") val recipientId: String,
    @SerializedName("attachment") var attachment: String? = null,
    @SerializedName("status") var status: MessageStatusType = MessageStatusType.SENDING,
    @SerializedName("deletedStatus") var deletedStatus: DeletedMessageStatusType = DeletedMessageStatusType.NOT_DELETED,
    @SerializedName("timestamp") var timestamp: Long = Date().time
){
    companion object {
        fun createMessage(content: String, chatId: String, senderId: String, recipientId: String): Message {
            return Message(
                messageId = UUID.randomUUID().toString(),
                chatId = chatId,
                content = content,
                senderId = senderId,
                recipientId = recipientId
            )
        }
    }

}
