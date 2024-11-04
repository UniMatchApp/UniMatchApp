package com.ulpgc.uniMatch.data.infrastructure.entities

import NotificationPayload
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.domain.enum.NotificationStatus
import com.ulpgc.uniMatch.data.domain.enum.NotificationType

@Entity(tableName = "notifications")
data class NotificationEntity (
    @PrimaryKey val id: String,
    val type: NotificationType,
    val status: NotificationStatus,
    val contentId: String,
    val payload: NotificationPayload,
    val date: Long,
    val recipient: String
)