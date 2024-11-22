package com.ulpgc.uniMatch.ui.screens.core.chat


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.chats.MessageBubble
import kotlinx.coroutines.launch

@Composable
fun ChatDetailScreen(
    chatId: String,
    chatViewModel: ChatViewModel,
    userViewModel: UserViewModel,
) {
    val messages by chatViewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Cargar mensajes al iniciar
    LaunchedEffect(chatId) {
        chatViewModel.loadMessages(chatId, messages.size)
    }

    // Detectar si el usuario está cerca del final de la lista
    val isFarFromBottom = remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= messages.size - 8
        }
    }

    val showScrollToBottomButton by remember {
        derivedStateOf {
            isFarFromBottom.value && messages.isNotEmpty()
        }
    }

    // Desplazar automáticamente hacia abajo cuando llegan nuevos mensajes
    LaunchedEffect(messages.size) {
        if (isFarFromBottom.value && messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(0.9f).fillMaxWidth(1f),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
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
        }
            MessageInput(
                viewModel = chatViewModel,
                chatId = chatId,
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth(1f)
                    .align(Alignment.BottomCenter)
            )

        if (!showScrollToBottomButton) {
            ScrollToBottomButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                },
                // Set the position fixed
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 72.dp)
                    .size(32.dp),
            )
        }
    }
}

@Composable
private fun ScrollToBottomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Scroll to bottom",
            modifier = Modifier.size(24.dp) // Tamaño del icono
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