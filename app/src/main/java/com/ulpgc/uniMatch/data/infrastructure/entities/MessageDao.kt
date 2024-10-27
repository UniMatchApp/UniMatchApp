package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getMessages(chatId: String, limit: Int, offset: Int): Flow<List<MessageEntity>> // Usar Flow para actualizaciones automáticas

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("UPDATE messages SET status = :status WHERE messageId = :messageId")
    suspend fun updateMessageStatus(messageId: String, status: MessageStatus)

    // Get latest message timestamp
    @Query("SELECT MAX(timestamp) FROM messages")
    suspend fun getLatestMessageTimestamp(): Long
}