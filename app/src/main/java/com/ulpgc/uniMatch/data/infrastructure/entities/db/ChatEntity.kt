package com.ulpgc.uniMatch.data.infrastructure.entities.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatPreviewData

// Entidad que representa los chats almacenados en la base de datos
@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,                    // Identificador del chat
    val userName: String,                          // Nombre del usuario del chat
    val lastMessage: String,                       // Último mensaje del chat
    val lastMessageTime: Long,                     // Timestamp del último mensaje
    val unreadMessagesCount: Int,                  // Número de mensajes no leídos
    val profileImageUrl: String                    // URL de la imagen de perfil del usuario
) {
    // Función para convertir ChatEntity a ChatPreviewData
    fun toPreviewData(): ChatPreviewData {
        return ChatPreviewData(
            id = id,
            userName = userName,
            lastMessage = lastMessage,
            lastMessageTime = lastMessageTime.toString(),
            unreadMessagesCount = unreadMessagesCount,
            profileImageUrl = profileImageUrl
        )
    }

    // Companion object para convertir ChatPreviewData a ChatEntity
    companion object {
        fun fromPreviewData(chatPreviewData: ChatPreviewData): ChatEntity {
            return ChatEntity(
                id = chatPreviewData.id,
                userName = chatPreviewData.userName,
                lastMessage = chatPreviewData.lastMessage,
                lastMessageTime = chatPreviewData.lastMessageTime.toLong(),
                unreadMessagesCount = chatPreviewData.unreadMessagesCount,
                profileImageUrl = chatPreviewData.profileImageUrl
            )
        }
    }
}

