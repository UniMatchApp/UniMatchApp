@file:JvmName("ChatListScreenKt")

package com.ulpgc.uniMatch.ui.screens.core.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.ui.components.chats.ChatList

@Composable
fun ChatListScreen(
    viewModel: ChatViewModel,
    onChatClick: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadChats()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val chatList by viewModel.chatList.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()

        if (isLoading) {
            CircularProgressIndicator()
        } else if (chatList.isEmpty()) {
            Text(text = "No chats available", style = MaterialTheme.typography.titleLarge)
        } else {
            ChatList(
                chats = chatList,
                onChatClick = { chat ->
                    // Navegar al detalle del chat al hacer clic en un chat
                    onChatClick(chat.userId)
                }
            )
        }
    }
}
