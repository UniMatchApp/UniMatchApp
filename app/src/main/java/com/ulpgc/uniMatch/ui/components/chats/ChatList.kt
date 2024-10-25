package com.ulpgc.uniMatch.ui.components.chats

import ChatListItem
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulpgc.uniMatch.data.infrastructure.entities.ChatPreviewData


@Composable
fun ChatList(
    chats: List<ChatPreviewData>,
    onChatClick: (ChatPreviewData) -> Unit
) {
    LazyColumn(
        modifier = Modifier.
        fillMaxWidth()

    ) {
        items(chats) { chat ->
            ChatListItem(
                profileImageUrl = chat.profileImageUrl,
                userName = chat.userName,
                lastMessage = chat.lastMessage,
                lastMessageTime = chat.lastMessageTime,
                unreadMessagesCount = chat.unreadMessagesCount,
                onChatClick = { onChatClick(chat) } // Llamar al callback con el chat seleccionado
            )
        }
    }
}
