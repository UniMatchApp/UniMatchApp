package com.ulpgc.uniMatch.data.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity

@Dao
interface ProfileDao {
    // Obtener un perfil por su ID
    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    fun getProfileById(profileId: String): ProfileEntity
}