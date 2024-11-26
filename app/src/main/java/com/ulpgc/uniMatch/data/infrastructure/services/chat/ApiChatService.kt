package com.ulpgc.uniMatch.data.infrastructure.services.chat


import com.ulpgc.uniMatch.data.application.services.ChatService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.MessageStatus
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

            val response = messageController.sendMessage(message)

            if (!response.success) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            chatMessageDao.setMessageStatus(message.messageId, MessageStatus.SENT)
            message.status = MessageStatus.SENT

            Result.success(message)
        }
    }

    override suspend fun saveMessage(message: Message): Result<Unit> {
        return safeRequest {
            // Insert the message into the local database
            chatMessageDao.insertMessage(MessageEntity.fromDomain(message))
            Result.success(Unit)
        }
    }

    override suspend fun getChats(loggedUserId: String): Result<List<Chat>> {
        return safeRequest {
            val chats = mutableListOf<Chat>()
            val dbChats: List<Chat> =
                chatMessageDao.getAllChatsOrderedByLastMessage().first().map { chatEntity ->
                    // Obtiene el último mensaje y el conteo de no leídos para cada chat
                    val lastMessageEntity = chatMessageDao.getLastMessageForChat(chatEntity.id)
                    val unreadMessagesCount = chatMessageDao.countUnreadMessages(
                        userId = loggedUserId,
                        chatId = chatEntity.id
                    )

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
                        userName = chatEntity.name,
                        profilePictureUrl = chatEntity.profilePictureUrl,
                        lastMessage = lastMessage,
                        unreadMessagesCount = unreadMessagesCount
                    )
                }

            chats.addAll(dbChats)

            // TODO: Create new empty chats for matching users from endpoint in backend
            val matchingUsers = matchingController.getMatchingUserIds();

            matchingUsers.value?.forEach { userId ->
                val profile = profileService.getProfile(userId).getOrElse { return@forEach }
                if (dbChats.none { it.userId == profile.userId }) {
                    chatMessageDao.insertChat(
                        ChatEntity(
                            id = profile.userId,
                            name = profile.name,
                            profilePictureUrl = profile.preferredImage
                        )
                    )
                }
            }
            var lastMessageTime =
                dbChats.maxByOrNull { it.lastMessage?.timestamp ?: 0 }?.lastMessage?.timestamp
                    ?: 0
            while (true) {
                val response =
                    messageController.getMessages(lastMessageTime)

                if (!response.success) {
                    throw Throwable(response.errorMessage ?: "Unknown error occurred")
                }
                if (response.value.isNullOrEmpty()) {
                    break
                }
                response.value.forEach { message ->
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
                    } else {
                        val profile = profileService.getProfile(userId)
                            .getOrElse { return@forEach } // Si no se puede obtener el perfil, omitir el mensaje
                        val newChat = Chat(
                            userId = userId,
                            userName = profile.name,
                            profilePictureUrl = profile.preferredImage,
                            lastMessage = message,
                            unreadMessagesCount = 1
                        )
                        chatMessageDao.insertChat(ChatEntity.fromDomain(newChat))
                        chats.add(newChat)
                    }

                }
                lastMessageTime = response.value.maxByOrNull { it.timestamp }?.timestamp ?: 0
            }
            Result.success(chats)
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
                        chatId = messageEntity.chatId,
                        content = messageEntity.content,
                        senderId = messageEntity.senderId,
                        recipientId = messageEntity.recipientId,
                        timestamp = messageEntity.timestamp,
                        status = messageEntity.status,
                        deletedStatus = messageEntity.deletedStatus,
                        attachment = messageEntity.attachment
                    )
                }


            Result.success(messages)
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

                Chat(
                    userId = chatEntity.id,
                    userName = chatEntity.name,
                    profilePictureUrl = chatEntity.profilePictureUrl,
                    lastMessage = lastMessage,
                    unreadMessagesCount = unreadMessagesCount
                )
            }

            Result.success(chats)
        }
    }

    override suspend fun setMessageStatus(
        loggedUserId: String,
        messageId: String,
        status: MessageStatus
    ): Result<Message> {
        return safeRequest {
            chatMessageDao.setMessageStatus(messageId, status)
            val response = messageController.modifyMessage(
                messageId,
                ModifyMessageDTO.createModifyMessage(
                    loggedUserId,
                    status = status
                )
            )
            if (!response.success || response.value == null) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }
            Result.success(
                Message(
                    messageId = messageId,
                    chatId = response.value.senderId,
                    content = response.value.content,
                    senderId = response.value.senderId,
                    recipientId = response.value.recipientId,
                    timestamp = response.value.timestamp,
                    status = response.value.status,
                    deletedStatus = response.value.deletedStatus,
                    attachment = response.value.attachment
                )
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
                ModifyMessageDTO.createModifyMessage(
                    userId,
                    content = newContent
                )
            )
            if (!response.success || response.value == null) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            Result.success(
                Message(
                    messageId = messageId,
                    chatId = response.value.senderId,
                    content = response.value.content,
                    senderId = response.value.senderId,
                    recipientId = response.value.recipientId,
                    timestamp = response.value.timestamp,
                    status = response.value.status,
                    deletedStatus = response.value.deletedStatus,
                    attachment = response.value.attachment
                )
            )
        }
    }

    override suspend fun deleteMessage(
        userId: String,
        messageId: String,
        deletedStatus: DeletedMessageStatus
    ): Result<Message> {
        return safeRequest {
            chatMessageDao.setMessageDeletedStatus(messageId, deletedStatus)
            val response = messageController.modifyMessage(
                messageId,
                ModifyMessageDTO.createModifyMessage(
                    userId,
                    deletedStatus = deletedStatus
                )
            )
            if (!response.success || response.value == null) {
                throw Throwable(response.errorMessage ?: "Unknown error occurred")
            }

            Result.success(
                Message(
                    messageId = messageId,
                    chatId = response.value.senderId,
                    content = response.value.content,
                    senderId = response.value.senderId,
                    recipientId = response.value.recipientId,
                    timestamp = response.value.timestamp,
                    status = response.value.status,
                    deletedStatus = response.value.deletedStatus,
                    attachment = response.value.attachment
                )
            )
        }
    }
}