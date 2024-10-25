package com.ulpgc.uniMatch.data.infrastructure.entities.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// DAO para manejar operaciones en la tabla de chats
@Dao
interface ChatDao {

    @Query("SELECT * FROM chats ORDER BY lastMessageTime DESC")
    fun getAllChats(): List<ChatEntity> // Devuelve todos los chats, ordenados por el último mensaje

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ChatEntity) // Inserta o actualiza un chat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChats(chats: List<ChatEntity>) // Inserta o actualiza múltiples chats

    @Update
    fun updateChat(chat: ChatEntity) // Actualiza la información de un chat

    @Query("DELETE FROM chats WHERE id = :chatId")
    fun deleteChat(chatId: String) // Elimina un chat específico

    @Query("DELETE FROM chats")
    fun deleteAllChats() // Elimina todos los chats

    @Query("SELECT * FROM chats WHERE id = :chatId")
    fun getChatById(chatId: String): ChatEntity? // Obtiene un chat por su ID



}