package com.ulpgc.uniMatch.data.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.EventEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.NotificationEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY 'likes' DESC")
    suspend fun getAllEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: String): EventEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()

    @Query("SELECT * FROM events WHERE LOWER(title) LIKE '%' || LOWER(:eventName) || '%'")
    suspend fun getEventsByName(eventName: String): List<EventEntity>


}