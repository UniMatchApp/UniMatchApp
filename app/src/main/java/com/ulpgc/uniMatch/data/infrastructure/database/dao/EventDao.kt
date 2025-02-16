package com.ulpgc.uniMatch.data.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ulpgc.uniMatch.data.infrastructure.entities.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY 'likes' DESC")
    fun getAllEvents(): List<EventEntity>
}