package com.ulpgc.uniMatch.ui.components.chats

import ChatListItem
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulpgc.uniMatch.data.domain.models.Chat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatList(
    chats: List<Chat>,
    onChatClick: (Chat) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(chats) { chat ->
            ChatListItem(
                profileImageUrl = chat.profilePictureUrl,
                userName = chat.userId,
                lastMessage = chat.lastMessage?.content ?: "",
                lastMessageTime = formatDate(chat.lastMessage?.timestamp ?: 0),
                unreadMessagesCount = chat.unreadMessagesCount,
                onChatClick = { onChatClick(chat) } // Llamar al callback con el chat seleccionado
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
