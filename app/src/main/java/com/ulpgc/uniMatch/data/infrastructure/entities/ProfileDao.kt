package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProfileDao {
    // Obtener un perfil por su ID
    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    fun getProfileById(profileId: String): ProfileEntity
}