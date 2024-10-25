package com.ulpgc.uniMatch.ui.screens.core.chat


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.infrastructure.services.auth.MockAuthService
import com.ulpgc.uniMatch.data.infrastructure.services.chat.MockChatService
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.ui.components.chats.MessageBubble
import com.ulpgc.uniMatch.ui.theme.UniMatchTheme

@Composable
fun ChatDetailScreen(
    chatId: String,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel
) {
    // Cargar los mensajes del chat cuando la pantalla se inicia
    LaunchedEffect(chatId) {
        chatViewModel.loadMessages(chatId)
    }

    val messages by chatViewModel.messages.collectAsState()
    val isLoading by chatViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            // Mostrar los mensajes
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(messages) { message ->
                    Log.i("ChatDetailScreen", "Message Sender: ${message.senderId}")
                    Log.i("ChatDetailScreen", "Current user: ${authViewModel.userId}")

                    MessageBubble(
                        message = message,
                        isCurrentUser = message.senderId == authViewModel.userId
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Input para enviar mensajes
            MessageInput(chatViewModel, chatId)
        }
    }
}

@Composable
private fun MessageInput(
    viewModel: ChatViewModel,
    chatId: String
) {
    var newMessage by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 0.dp)
            .height(56.dp) // Aumenta la altura para asegurar espacio para el texto
    ) {
        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
                .clip(CircleShape), // Hacer que el TextField tenga bordes redondeados
            shape = MaterialTheme.shapes.medium, // Ajusta la forma del TextField
            textStyle = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                viewModel.sendMessage(chatId, newMessage)
                newMessage = ""
            },
            modifier = Modifier
                .size(46.dp) // Hace que el botón sea cuadrado y redondeado
                .clip(CircleShape), // Lo hace redondo
            contentPadding = PaddingValues(0.dp) // Elimina el padding interno del botón
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                modifier = Modifier.size(24.dp), // Ajusta el tamaño del icono
                contentDescription = "Send message"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewChatDetailScreen() {
    UniMatchTheme {
        ChatDetailScreen(
            chatId = "sampleChatId",
            chatViewModel = ChatViewModel(
                chatService = MockChatService(),
                errorViewModel = ErrorViewModel()
            ),
            authViewModel = AuthViewModel(
                authService = MockAuthService(),
                errorViewModel = ErrorViewModel()
            )
        )
    }
}

