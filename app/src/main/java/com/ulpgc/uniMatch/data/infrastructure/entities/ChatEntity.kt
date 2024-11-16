package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.domain.models.Chat

// Entidad que representa los chats almacenados en la base de datos
@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,                          // Nombre del usuario del chat
) {

    // Companion object para convertir ChatPreviewData a ChatEntity
    companion object {
        fun fromDomain(chat: Chat): ChatEntity {
            return ChatEntity(
                id = chat.userId,
            )
        }
    }
}

