package com.ulpgc.uniMatch.data.domain.models

data class ChatPreviewData(
    val id: String,
    val userName: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadMessagesCount: Int,
    val profileImageUrl: String
) {
}
