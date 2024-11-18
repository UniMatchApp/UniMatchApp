package com.ulpgc.uniMatch.data.infrastructure.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ChatMessageDao
import com.ulpgc.uniMatch.data.infrastructure.database.dao.MessageDao
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.MessageEntity
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity

@Database(entities = [ProfileEntity::class, ChatEntity::class, MessageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun profileDao(): ProfileDao
    abstract fun messageDao(): MessageDao


    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "uniMatch_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
