package com.ulpgc.uniMatch.data.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ulpgc.uniMatch.data.infrastructure.entities.MatchingEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity

@Dao
interface ProfileDao {
    // Obtener un perfil por su ID
    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    suspend fun getProfileById(profileId: String): ProfileEntity?

    // Insert un perfil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    // Inserta un matching
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatching(matching: MatchingEntity)

    // Obtiene todos los perfiles de matching
    @Query("SELECT * FROM matching")
    suspend fun getAllMatching(): List<MatchingEntity>

    // Elimina un perfil de matching
    @Query("DELETE FROM matching WHERE profileId = :profileId")
    suspend fun deleteProfile(profileId: String)

}