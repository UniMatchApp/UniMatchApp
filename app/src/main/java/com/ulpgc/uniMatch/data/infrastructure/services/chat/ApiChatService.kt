package com.ulpgc.uniMatch.data.infrastructure.services.chat


import android.util.Log
import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.models.Chat
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.domain.models.ModifyMessageDTO
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController
import com.ulpgc.uniMatch.data.infrastructure.controllers.MessageController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ChatMessageDao
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageEntity
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
import kotlinx.coroutines.flow.first
import java.util.UUID


class ApiChatService(
    private val messageController: MessageController,
    private val matchingController: MatchingController,
    private val chatMessageDao: ChatMessageDao,
    private val profileService: ProfileService,
) : ChatService {
    override suspend fun sendMessage(
        loggedUserId: String,
        chatId: String,
        content: String,
        attachment: String?
    ): Result<Message> {
        return safeRequest {
            val message = Message(
                messageId = UUID.randomUUID().toString(),
                content = content,
                senderId = loggedUserId,
                recipientId = chatId,
                attachment = attachment,
                receptionStatus = ReceptionStatus.SENDING
            )
            chatMessageDao.insertMessages(listOf(MessageEntity.fromDomain(message, loggedUserId)))

            val result = runCatching {
                messageController.sendMessage(message)
            }

            if (result.isFailure) {
                chatMessageDao.setMessageStatus(message.messageId, ReceptionStatus.FAILED)
                message.receptionStatus = ReceptionStatus.FAILED
                Log.e("ApiChatService", "Message failed: $message")
            } else {
                val response = result.getOrNull()
                val messageId = response?.value?.messageId
                chatMessageDao.setMessageStatus(message.messageId, ReceptionStatus.SENT)
                message.receptionStatus = ReceptionStatus.SENT
                chatMessageDao.updateChatId(message.messageId, messageId ?: message.messageId)
                message.messageId = messageId ?: message.messageId
                Log.i("ApiChatService", "Message sent: $message")
            }

            return@safeRequest message
        }.mapCatching { message ->
            message
        }
    }


    override suspend fun saveMessage(message: Message, loggedUserId: String): Result<Unit> {
        return safeRequest {
            // Insert the message into the local database
            chatMessageDao.insertMessage(MessageEntity.fromDomain(message, loggedUserId))
        }
    }

    override suspend fun messageHasBeenRead(message: Message, loggedUserId: String): Result<Unit> {
        return safeRequest {
            if ((message.receptionStatus == ReceptionStatus.RECEIVED || message.receptionStatus == ReceptionStatus.SENT) && message.recipientId == loggedUserId) {
                Log.i("ApiChatService", "Message has been read: $message")
                val result = messageController.messageHasBeenRead(message.messageId)
                if (!result.success) {
                    throw Throwable(result.errorMessage ?: "Unknown error occurred")
                }

                chatMessageDao.setMessageStatus(message.messageId, ReceptionStatus.READ)
            }
        }.mapCatching { }
    }

    override suspend fun messageHasBeenReceived(message: Message, loggedUserId: String): Result<Unit> {
        return safeRequest {
            if (message.receptionStatus == ReceptionStatus.SENT && message.recipientId == loggedUserId) {
                Log.i("ApiChatService", "Message has been received: $message")
                val result = messageController.messageHasBeenReceived(message.messageId)
                if (!result.success) {
                    throw Throwable(result.errorMessage ?: "Unknown error occurred")
                }

                chatMessageDao.setMessageStatus(message.messageId, ReceptionStatus.RECEIVED)
            }
        }.mapCatching { }
    }

    override suspend fun getChats(loggedUserId: String): Result<List<Chat>> {
        return safeRequest {
            val dbChats = chatMessageDao.getAllChatsOrderedByLastMessage().first().map { chatEntity ->
                val lastMessageEntity = chatMessageDao.getLastMessageForChat(chatEntity.id)
                val unreadMessagesCount = chatMessageDao.countUnreadMessages(loggedUserId, chatEntity.id)

                // Crea el objeto de dominio Chat
                Chat(
                    userId = chatEntity.id,
                    userName = chatEntity.name,
                    profilePictureUrl = chatEntity.profilePictureUrl,
                    lastMessage = lastMessageEntity?.let { MessageEntity.toDomain(it) },
                    unreadMessagesCount = unreadMessagesCount
                )
            }.toMutableList()

            // Crear chats para usuarios coincidentes si no existen
            matchingController.getMatchingUserIds().value?.forEach { userId ->
                profileService.getProfile(userId).onSuccess { profile ->
                    if (dbChats.none { it.userId == profile.userId }) {
                        chatMessageDao.insertChat(ChatEntity(id = profile.userId, name = profile.name, profilePictureUrl = profile.preferredImage))
                        dbChats.add(Chat(userId = profile.userId, userName = profile.name, profilePictureUrl = profile.preferredImage, lastMessage = null, unreadMessagesCount = 0))
                    }
                }
            }

            // Obtener nuevos mensajes y actualizar chats
            var lastMessageTime = dbChats.maxOfOrNull { it.lastMessage?.createdAt ?: 0 } ?: 0
            while (true) {
                val response = messageController.getMessages(lastMessageTime)
                if (!response.success || response.value.isNullOrEmpty()) break

                response.value.forEach { message ->
                    val userId = if (message.senderId != loggedUserId) message.senderId else message.recipientId
                    if (message.senderId == loggedUserId && message.receptionStatus == ReceptionStatus.SENT) {
                        messageHasBeenReceived(message, loggedUserId)
                        chatMessageDao.setMessageStatus(message.messageId, ReceptionStatus.RECEIVED)
                    }
                    val chatEntity = chatMessageDao.getChatById(userId)

                    if (chatEntity != null) {
                        // Actualiza mensaje en chat existente
                        chatMessageDao.insertMessage(MessageEntity.fromDomain(message, loggedUserId))
                        dbChats.find { it.userId == userId }?.let { chat ->
                            chat.lastMessage = message
                            chat.unreadMessagesCount += if (message.receptionStatus == ReceptionStatus.SENT && message.recipientId == loggedUserId) 1 else 0
                        }

                    } else {
                        profileService.getProfile(userId).onSuccess { profile ->
                            val newChat = Chat(
                                userId = userId,
                                userName = profile.name,
                                profilePictureUrl = profile.preferredImage,
                                lastMessage = message,
                                unreadMessagesCount = 1
                            )
                            chatMessageDao.insertChat(ChatEntity.fromDomain(newChat))
                            dbChats.add(newChat)
                        }
                    }
                }

                lastMessageTime = response.value.maxOfOrNull { maxOf(it.createdAt, it.updatedAt) } ?: 0
            }

            return@safeRequest dbChats
        }
    }

    override suspend fun getMessages(
        chatId: String,
        offset: Int,
        limit: Int
    ): Result<List<Message>> {
        return safeRequest {
            val messages =
                chatMessageDao.getMessages(chatId, limit, offset).first().map { messageEntity ->
                    Message(
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
            return@safeRequest messages
        }

    }

    override suspend fun getChatsByName(
        loggedUserId: String,
        filterName: String
    ): Result<List<Chat>> {
        return safeRequest {
            // Obtener la lista de chats que coinciden con el nombre de usuario
            val chatEntities = chatMessageDao.getChatsByUserName(filterName)

            // Transformar cada ChatEntity en un Chat con sus mensajes y conteos correspondientes
            val chats = chatEntities.map { chatEntity ->
                val lastMessageEntity = chatMessageDao.getLastMessageForChat(chatEntity.id)
                val unreadMessagesCount = chatMessageDao.countUnreadMessages(
                    userId = loggedUserId,
                    chatId = chatEntity.id
                )

                val lastMessage = lastMessageEntity?.let {
                    Message(
                        messageId = it.messageId,
                        content = it.content,
                        senderId = it.senderId,
                        recipientId = it.recipientId,
                        createdAt = it.timestamp,
                        receptionStatus = it.receptionStatus,
                        contentStatus = it.contentStatus,
                        deletedStatus = it.deletedStatus,
                        attachment = it.attachment
                    )
                }

                Chat(
                    userId = chatEntity.id,
                    userName = chatEntity.name,
                    profilePictureUrl = chatEntity.profilePictureUrl,
                    lastMessage = lastMessage,
                    unreadMessagesCount = unreadMessagesCount
                )
            }
            return@safeRequest chats
        }
    }

    override suspend fun setMessageStatus(
        loggedUserId: String,
        messageId: String,
        status: ReceptionStatus
    ): Result<Message> {
        return safeRequest {
            chatMessageDao.setMessageStatus(messageId, status)
            val response = messageController.modifyMessage(
                messageId,
                ModifyMessageDTO.create(
                    loggedUserId,
                    status = status
                )
            )
            if (!response.success || response.value == null) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }
            return@safeRequest Message(
                messageId = messageId,
                content = response.value.content,
                senderId = response.value.senderId,
                recipientId = response.value.recipientId,
                createdAt = response.value.createdAt,
                receptionStatus = response.value.receptionStatus,
                contentStatus = response.value.contentStatus,
                deletedStatus = response.value.deletedStatus,
                attachment = response.value.attachment
            )

        }
    }

    override suspend fun editMessageContent(
        userId: String,
        messageId: String,
        newContent: String
    ): Result<Message> {
        return safeRequest {
            chatMessageDao.setMessageContent(messageId, newContent)
            val response = messageController.modifyMessage(
                messageId,
                ModifyMessageDTO.create(newContent)
            )
            if (!response.success || response.value == null) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest Message(
                messageId = messageId,
                content = response.value.content,
                senderId = response.value.senderId,
                recipientId = response.value.recipientId,
                createdAt = response.value.createdAt,
                receptionStatus = response.value.receptionStatus,
                contentStatus = response.value.contentStatus,
                deletedStatus = response.value.deletedStatus,
                attachment = response.value.attachment
            )
        }
    }

    override suspend fun deleteMessage(
        userId: String,
        messageId: String,
        deletedStatus: DeletedMessageStatus,
    ): Result<Message> {
        return safeRequest {
            chatMessageDao.setMessageDeletedStatus(messageId, deletedStatus)
            val response = messageController.modifyMessage(
                messageId,
                ModifyMessageDTO.create(
                    userId,
                    deletedStatus = deletedStatus
                )
            )
            if (!response.success || response.value == null) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest Message(
                messageId = messageId,
                content = response.value.content,
                senderId = response.value.senderId,
                recipientId = response.value.recipientId,
                createdAt = response.value.createdAt,
                receptionStatus = response.value.receptionStatus,
                contentStatus = response.value.contentStatus,
                deletedStatus = response.value.deletedStatus,
                attachment = response.value.attachment
            )
        }
    }
}