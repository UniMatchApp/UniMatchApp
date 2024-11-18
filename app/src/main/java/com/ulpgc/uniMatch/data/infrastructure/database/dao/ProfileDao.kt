package com.ulpgc.uniMatch.data.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity

@Dao
interface ProfileDao {
    // Obtener un perfil por su ID
    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    suspend fun getProfileById(profileId: String): ProfileEntity?

    // Insert un perfil
    @Insert
    suspend fun insertProfile(profile: ProfileEntity)
}