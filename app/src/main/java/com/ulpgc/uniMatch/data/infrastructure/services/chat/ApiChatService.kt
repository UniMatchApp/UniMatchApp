package com.ulpgc.uniMatch.data.infrastructure.services.chat


import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.MessageStatusType
import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.infrastructure.controllers.MessageController
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatEntity
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ChatMessageDao
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageEntity
import kotlinx.coroutines.flow.first
import java.util.UUID


class ApiChatService(
    private val messageController: MessageController,
    private val chatMessageDao: ChatMessageDao,
    private val profileService: ProfileService,
) : ChatService {
    override suspend fun sendMessage(
        loggedUserId: String,
        chatId: String,
        content: String,
        attachment: String?
    ): Result<Message> {
        return try {
            // First we save the message to the local database
            val message = Message(
                messageId = UUID.randomUUID().toString(),
                content = content,
                chatId = chatId,
                senderId = loggedUserId,
                recipientId = chatId,
                attachment = attachment
            )
            chatMessageDao.insertMessages(listOf(MessageEntity.fromDomain(message)))

            // Then we must try to send the message to the server

            val response = messageController.sendMessage("Bearer token", message)

            if (!response.success) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            chatMessageDao.setMessageStatus(message.messageId, MessageStatusType.SENT)

            Result.success(message)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChats(loggedUserId: String): Result<List<Chat>> {
        return try {
            val chats = mutableListOf<Chat>()

            val dbChats: List<Chat> =
                chatMessageDao.getAllChatsOrderedByLastMessage().first().map { chatEntity ->
                    // Obtiene el último mensaje y el conteo de no leídos para cada chat
                    val lastMessageEntity = chatMessageDao.getLastMessageForChat(chatEntity.id)
                    val unreadMessagesCount = chatMessageDao.countUnreadMessages(chatEntity.id)

                    // Mapea el último mensaje al modelo de dominio si existe
                    val lastMessage = lastMessageEntity?.let {
                        Message(
                            messageId = it.messageId,
                            chatId = it.chatId,
                            content = it.content,
                            senderId = it.senderId,
                            recipientId = it.recipientId,
                            timestamp = it.timestamp,
                            status = it.status,
                            deletedStatus = it.deletedStatus,
                            attachment = it.attachment
                        )
                    }
                    // Retorna un objeto Chat del dominio con todos los datos necesarios
                    Chat(
                        userId = chatEntity.id,
                        profilePictureUrl = "",
                        lastMessage = lastMessage,
                        unreadMessagesCount = unreadMessagesCount
                    )
                }

            // Find the most recent message among all chats
            val lastMessageTime =
                dbChats.maxByOrNull { it.lastMessage?.timestamp ?: 0 }?.lastMessage?.timestamp

            // TODO: Create new empty chats for matching users from endpoint in backend
            val matchingUsers = messageController.getMatchingUserIds(loggedUserId);

            matchingUsers.value?.forEach { userId ->
                if (dbChats.none { it.userId == userId }) {
                    chatMessageDao.insertChat(ChatEntity(id = userId))
                }
            }

            val response = messageController.getMessages("Bearer token", lastMessageTime)

            if (!response.success) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }
            val messages = response.value ?: emptyList()

            messages.forEach { message ->
                val userId = (message.senderId != loggedUserId).let {
                    if (it) message.senderId else message.recipientId
                }
                val chatEntity = chatMessageDao.getChatByUserId(userId)

                if (chatEntity != null) {
                    // Si el chat ya existe, actualizarlo
                    chatMessageDao.insertMessage(
                        MessageEntity.fromDomain(
                            Message(
                                messageId = message.messageId,
                                chatId = chatEntity.id,
                                content = message.content,
                                senderId = message.senderId,
                                recipientId = message.recipientId,
                                timestamp = message.timestamp,
                                status = message.status,
                                deletedStatus = message.deletedStatus,
                                attachment = message.attachment
                            )
                        )
                    )
                    return@forEach
                }
                chatMessageDao.insertChat(
                    ChatEntity(id = userId)
                )
                chats.add(
                    Chat(
                        userId = userId,
                        profilePictureUrl = profileService.getProfile(userId)
                            .getOrNull()?.preferredImage ?: "",
                        lastMessage = message,
                        unreadMessagesCount = 1
                    )
                )

            }

            Result.success(chats)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun getChatsByName(chatName: String): Result<List<Chat>> {
        TODO("Not yet implemented")
    }
}