package com.ulpgc.uniMatch.data.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.infrastructure.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDao {
    @Query("SELECT * FROM notifications WHERE recipient = :userId ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getNotifications(userId: String, limit: Int, offset: Int): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET status = :status WHERE id = :notificationId")
    suspend fun updateNotificationStatus(notificationId: String, status: NotificationStatus)

    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: String)

    @Query("DELETE FROM notifications WHERE recipient = :userId")
    suspend fun deleteAllNotifications(userId: String)
}