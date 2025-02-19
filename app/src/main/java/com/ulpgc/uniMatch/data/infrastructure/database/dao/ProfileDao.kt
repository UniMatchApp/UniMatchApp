package com.ulpgc.uniMatch.data.infrastructure.database.dao

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import com.ulpgc.uniMatch.data.infrastructure.database.AppDatabase
import com.ulpgc.uniMatch.data.infrastructure.entities.MatchingEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "uniMatch_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(appDatabase: AppDatabase): ProfileDao {
        return appDatabase.profileDao()
    }
}
