package com.ulpgc.uniMatch.data.infrastructure.services.chat


import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.domain.models.ChatPreviewData
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.infrastructure.controllers.MessageController
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatDao
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageDao


class ApiChatService(
    private val messageController: MessageController,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ChatService {

    override suspend fun getChats(): Result<List<ChatPreviewData>> {
        return try {
            val chats = mutableListOf<ChatPreviewData>()

            val dbChats = chatDao.getAllChats().map { it.toPreviewData() }

            chats.addAll(dbChats)

            val lastMessageTime = messageDao.getLatestMessageTimestamp()

            val response = messageController.getMessages("Bearer token", lastMessageTime)

            if (!response.success) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            val messages = response.value ?: emptyList()

            val messageEntities = messages.map { it.toEntity() }
            messageDao.insertMessages(messageEntities)

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

            Result.success(chats)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        TODO("Not yet implemented")
    }
}