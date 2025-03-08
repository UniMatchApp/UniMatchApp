package com.ulpgc.uniMatch.data.domain.models.notification

import NotificationPayload
import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus
import com.ulpgc.uniMatch.data.domain.enums.NotificationType
import com.ulpgc.uniMatch.data.infrastructure.entities.NotificationEntity

data class Notification (
    val id: String,
    var status: NotificationStatus,
    val contentId: String,
    val payload: NotificationPayload,
    val date: Long,
    val recipient: String
) {

}