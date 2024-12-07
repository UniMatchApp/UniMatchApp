package com.ulpgc.uniMatch.data.infrastructure.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ulpgc.uniMatch.data.domain.enums.DeletedMessageStatus
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    // Inserción de chat y mensajes en una transacción
    @Transaction
    suspend fun insertChatWithMessages(chat: ChatEntity, messages: List<MessageEntity>) {
        insertChat(chat)
        insertMessages(messages)
    }

    @Query("SELECT * FROM chats WHERE id = :senderId")
    suspend fun getChatByUserId(senderId: String): ChatEntity?

    // Obtiene un chat por su id
    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: String): ChatEntity?

    @Query("SELECT * FROM chats WHERE LOWER(name) LIKE '%' || LOWER(:userName) || '%'")
    suspend fun getChatsByUserName(userName: String): List<ChatEntity>

    // Obtiene todos los chats, ordenados por la hora del último mensaje
    @Query(
        """
        SELECT * FROM chats 
        ORDER BY (
            SELECT MAX(timestamp) FROM messages WHERE messages.chatId = chats.id
        ) DESC
    """
    )
    fun getAllChatsOrderedByLastMessage(): Flow<List<ChatEntity>>

    // Inserta o actualiza un chat
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    // Inserta o actualiza múltiples mensajes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    // Inserta un solo mensaje y actualiza el conteo de mensajes no leídos en el chat correspondiente

    // Inserta un único mensaje
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)


    @Query("UPDATE messages SET content = :content WHERE messageId = :messageId")
    suspend fun setMessageContent(messageId: String, content: String)

    // Actualiza el conteo de mensajes no leídos en el chat (status != READ)
    @Query("SELECT COUNT(*) FROM messages WHERE chatId = :chatId AND receptionStatus != :status AND recipientId == :userId")
    suspend fun countUnreadMessages(
        userId: String,
        chatId: String,
        status: ReceptionStatus = ReceptionStatus.READ
    ): Int

    // Obtiene los mensajes de un chat específico, con paginación
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    fun getMessages(chatId: String, limit: Int, offset: Int): Flow<List<MessageEntity>>

    // Obtiene el último mensaje de un chat
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessageForChat(chatId: String): MessageEntity?

    // Marca todos los mensajes como leídos en un chat y actualiza el contador
    @Transaction
    suspend fun markMessagesAsRead(chatId: String) {
        setMessageStatus(chatId, ReceptionStatus.READ)
    }

    @Query("UPDATE messages SET receptionStatus = :status WHERE messageId = :chatId AND receptionStatus != :status")
    suspend fun setMessageStatus(chatId: String, status: ReceptionStatus)


    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChat(chatId: String)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: String)

    @Query("UPDATE messages SET deletedStatus = :deletedStatus WHERE messageId = :messageId")
    suspend fun setMessageDeletedStatus(messageId: String, deletedStatus: DeletedMessageStatus)
}
