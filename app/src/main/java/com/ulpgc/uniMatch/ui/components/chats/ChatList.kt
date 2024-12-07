package com.ulpgc.uniMatch.ui.components.chats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enums.ChatStatus
import com.ulpgc.uniMatch.data.domain.models.Chat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ChatList(
    chats: List<Chat>,
    onChatClick: (Chat) -> Unit,
    userStatusMap: Map<String, ChatStatus>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(chats) { chat ->
            val lastMessage = chat.lastMessage?.content ?: stringResource(R.string.start_chat)
            val lastMessageTime = if (chat.lastMessage != null) {
                formatDate(chat.lastMessage!!.timestamp)
            } else {
                "" // No mostrar fecha si no hay mensaje
            }

            ChatListItem(
                profileImageUrl = chat.profilePictureUrl,
                userName = chat.userName,
                lastMessage = lastMessage,
                lastMessageTime = lastMessageTime,
                unreadMessagesCount = chat.unreadMessagesCount,
                onChatClick = { onChatClick(chat) },
                userStatus = userStatusMap[chat.userId]
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val currentDate = Date() // Fecha actual
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return if (isSameDay(date, currentDate)) {
        // Si es el mismo día, solo mostrar la hora
        timeFormat.format(date)
    } else {
        // Si es otro día, mostrar la fecha completa
        dateFormat.format(date)
    }
}

private fun isSameDay(date1: Date, date2: Date): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = date1 }
    val calendar2 = Calendar.getInstance().apply { time = date2 }
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}
