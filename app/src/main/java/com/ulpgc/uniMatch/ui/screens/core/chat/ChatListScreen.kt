@file:JvmName("ChatListScreenKt")

package com.ulpgc.uniMatch.ui.screens.core.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun CustomStyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(Color.Transparent)
            .padding(10.dp)

            .then(Modifier.padding(10.dp)),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,

        ),
        shape = RoundedCornerShape(10.dp),
    )
}


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


        val chatList by viewModel.chatPreviewDataList.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val search by viewModel.searchQuery.collectAsState()
        var searchText by remember { mutableStateOf("") } // Estado local para el texto de búsqueda
        val coroutineScope = rememberCoroutineScope() // Coroutine scope para manejar el debounce

        CustomStyledTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                viewModel.updateSearchQuery(searchText) // Actualiza el query
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

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
