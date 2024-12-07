package com.ulpgc.uniMatch.ui.screens.core.chat


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.chats.MessageBubble
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    LaunchedEffect(messages) {
        Log.i("ChatDetailScreen", "Messages: $messages")
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
        ) {
            LazyColumn(
                state = listState,

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Ocupa el espacio sobrante
            ) {
                items(messages) { message ->
                    MessageBubble(
                        message = message,
                        isCurrentUser = message.senderId == userViewModel.userId
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (messages.isNotEmpty() && !showScrollToBottomButton) {
                ScrollToBottomButton(
                    onClick = {
                        coroutineScope.launch {
                            if (messages.isNotEmpty()) {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Transparent)
                        .align(Alignment.End)

                )

            }
        }



        MessageInput(
            viewModel = chatViewModel,
            chatId = chatId,
            modifier = Modifier
                .fillMaxWidth(),
            onTyping = { typing ->
                if (typing) {
                    userViewModel.userId?.let { chatViewModel.setUserTyping(chatId) }
                } else {
                    chatViewModel.setUserStoppedTyping()
                }
            },
            onStoppedTyping = {
                chatViewModel.setUserStoppedTyping()
            }
        )
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
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Scroll to bottom",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun MessageInput(
    viewModel: ChatViewModel,
    chatId: String,
    onTyping: (Boolean) -> Unit,
    onStoppedTyping: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newMessage by remember { mutableStateOf("") }
    var typing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var typingJob: Job? by remember { mutableStateOf(null) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        TextField(
            value = newMessage,
            onValueChange = {
                newMessage = it
                typing = it.isNotEmpty()
                if (it.isNotEmpty()) {
                    typingJob?.cancel()
                    typingJob = coroutineScope.launch {
                        delay(3000)
                        onStoppedTyping()
                    }
                    onTyping(true)
                } else {
                    onTyping(false)
                }
            },
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface)
                .clip(CircleShape),
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.bodyLarge,

            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                viewModel.sendMessage(chatId, newMessage, null)
                newMessage = ""
                typing = false
                onStoppedTyping()
                onTyping(false)
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


