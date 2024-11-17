package com.ulpgc.uniMatch.data.domain.models

data class Chat(
    val userId: String,
    val userName: String,
    val profilePictureUrl: String? = null,
    val lastMessage: Message? = null,
    val unreadMessagesCount: Int = 0
)