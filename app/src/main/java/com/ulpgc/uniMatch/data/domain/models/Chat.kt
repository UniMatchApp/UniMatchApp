package com.ulpgc.uniMatch.data.domain.models

data class Chat(
    val userId: String,
    val userName: String,
    val profilePictureUrl: String? = null,
    var lastMessage: Message? = null,
    var unreadMessagesCount: Int = 0
)