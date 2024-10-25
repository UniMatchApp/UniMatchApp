package com.ulpgc.uniMatch.data.infrastructure.services.chat

import com.ulpgc.uniMatch.data.application.ApiEndpoints
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatPreviewData
import com.ulpgc.uniMatch.data.infrastructure.entities.Message
import com.ulpgc.uniMatch.data.infrastructure.entities.db.ChatDao
import com.ulpgc.uniMatch.data.infrastructure.entities.db.ChatEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.db.MessageDao
import com.ulpgc.uniMatch.data.infrastructure.entities.db.MessageEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.db.MessageStatus


class ApiChatService(
    private val apiEndpoints: ApiEndpoints,
    private val chatDao: ChatDao, // DAO para manejar la base de datos local
    private val messageDao: MessageDao // DAO para manejar la base de datos local
) : ChatService {

    override suspend fun getChats(): Result<List<ChatPreviewData>> {
        return try {
            // Lista vacía que contendrá los chats para retornar
            val chats = mutableListOf<ChatPreviewData>()

            // Obtener los chats de la base de datos local
            val dbChats = chatDao.getAllChats().map { it.toPreviewData() }

            // Añadir los chats locales a la lista que se retornará
            chats.addAll(dbChats)

            // Obtener el timestamp del último mensaje almacenado en la base de datos
            val lastMessageTime = messageDao.getLatestMessageTimestamp()

            // Obtener los mensajes más recientes del backend
            val response = apiEndpoints.getMessages("Bearer token", lastMessageTime)

            // Verificar si la respuesta fue exitosa
            if (!response.success) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            val messages = response.value ?: emptyList()

            // Almacenar los nuevos mensajes en la base de datos
            val messageEntities = messages.map { it.toEntity() }
            messageDao.insertMessages(messageEntities)

            // Actualizar o crear los chats en la base de datos en función de los nuevos mensajes
            messages.forEach { message ->
                val chatEntity = chatDao.getChatById(message.chatId)

                if (chatEntity == null) {
                    // TODO: Obtener el nombre de usuario y la URL de la imagen de perfil del usuario
                    val newChat = ChatEntity(
                        id = message.chatId,
                        userName = message.senderId,
                        lastMessage = message.content,
                        lastMessageTime = message.timestamp,
                        unreadMessagesCount = 1,
                        profileImageUrl = ""
                    )
                    chatDao.insertChat(newChat)
                    chats.add(newChat.toPreviewData())
                } else {
                    // Si el chat ya existe, actualizarlo
                    chatDao.updateChat(
                        chatEntity.copy(
                            lastMessage = message.content,
                            lastMessageTime = message.timestamp,
                            unreadMessagesCount = chatEntity.unreadMessagesCount + 1
                        )
                    )
                }
            }

            // Devolver la lista de chats actualizada (local + mensajes recientes)
            Result.success(chats)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        TODO("Not yet implemented")
    }
}