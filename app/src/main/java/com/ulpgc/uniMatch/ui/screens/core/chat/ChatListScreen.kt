package com.ulpgc.uniMatch.ui.screens.core.chat


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
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
    authViewModel: AuthViewModel,
    navController: NavController
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
        // Top Bar con la foto del usuario, nombre y flecha de retroceso
        TopBar(
            userName = "User Name",
            onBackPressed = { navController.popBackStack() },
            userImage = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            // Mostrar los mensajes
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(messages) { message ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(userName: String, onBackPressed: () -> Unit, userImage: Int) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                // Icono de retroceso
                IconButton(onClick = onBackPressed) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back), // Reemplaza con tu icono de flecha
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Imagen del usuario (comentada)
                // Image(
                //    painter = painterResource(id = userImage),
                //    contentDescription = "User profile picture",
                //    modifier = Modifier
                //        .size(40.dp)
                //        .clip(CircleShape) // Hacer que la imagen sea redonda
                // )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = userName,
                    color = Color.White
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue
        )
    )
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
            .height(56.dp)
    ) {
        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
                .clip(CircleShape),
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                viewModel.sendMessage(chatId, newMessage)
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


@Preview(showBackground = true)
@Composable
fun PreviewChatDetailScreen() {
    UniMatchTheme {
        ChatDetailScreen(
            chatId = "sampleChatId",
            chatViewModel = ChatViewModel(
                chatService = MockChatService(),
                errorViewModel = ErrorViewModel(),
                authViewModel = AuthViewModel(
                    authService = MockAuthService(),
                    errorViewModel = ErrorViewModel()
                )
            ),
            authViewModel = AuthViewModel(
                authService = MockAuthService(),
                errorViewModel = ErrorViewModel(),
            ),
            navController = NavController(
                context = LocalContext.current
            )
        )
    }
}

