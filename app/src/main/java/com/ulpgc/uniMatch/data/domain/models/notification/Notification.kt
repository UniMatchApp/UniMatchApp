package com.ulpgc.uniMatch.data.domain.models.notification

import com.ulpgc.uniMatch.data.domain.enums.NotificationStatus

data class Notification (
    val id: String,
    var status: NotificationStatus,
    val contentId: String,
    val payload: NotificationPayload,
    val date: Long,
    val recipient: String
) {

}