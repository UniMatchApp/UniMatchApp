package com.ulpgc.uniMatch.ui.screens.core.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val chatList by viewModel.chatPreviewDataList.collectAsState()
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
                    onChatClick(chat.id)
                }
            )
        }
    }
}
