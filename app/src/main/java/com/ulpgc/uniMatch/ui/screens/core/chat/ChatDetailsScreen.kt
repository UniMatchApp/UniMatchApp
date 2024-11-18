package com.ulpgc.uniMatch.ui.screens.core.chat


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.ui.components.chats.MessageBubble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ChatDetailScreen(
    chatId: String,
    chatViewModel: ChatViewModel,
    userViewModel: UserViewModel,
) {
    val messages by chatViewModel.messages.collectAsState()
    LaunchedEffect(chatId) {
        withContext(Dispatchers.IO) {
            chatViewModel.loadMessages(chatId, messages.size)
        }
    }

    Column(
        modifier = Modifier
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top=8.dp, bottom=4.dp, start=16.dp, end=16.dp)
                .weight(1f)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isCurrentUser = message.senderId == userViewModel.userId
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        MessageInput(
            viewModel = chatViewModel,
            chatId = chatId,
        )
    }
}

@Composable
private fun MessageInput(
    viewModel: ChatViewModel,
    chatId: String,
    modifier: Modifier = Modifier
) {
    var newMessage by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            modifier = Modifier
                .weight(1f)
                .clip(CircleShape),
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                viewModel.sendMessage(chatId, newMessage, null)
                newMessage = ""
            },
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                modifier = Modifier.size(24.dp),
                contentDescription = "Send message"
            )
        }
    }
}