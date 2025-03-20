@file:JvmName("ChatListScreenKt")

package com.ulpgc.uniMatch.ui.screens.core.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.mocks.ChatPreviewDataMock.createChatPreviewDataMocks
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.ui.components.chats.ChatList

@Composable
fun ChatListScreen(
    viewModel: ChatViewModel,
    onChatClick: (String) -> Unit
) {

    val usersStatus = viewModel.usersStatus.collectAsState()
    val searchText = remember { mutableStateOf("") }
    val isSearchActive = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadChats()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val chatList by viewModel.chatList.observeAsState(emptyList())
        val isLoading by viewModel.isLoading.collectAsState()

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (chatList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.no_chats), style = MaterialTheme.typography.titleLarge)
            }
        } else {
            ChatList(
                chats = chatList,
                onChatClick = { chat ->
                    onChatClick(chat.userId)
                },
                onChatDelete = { chat ->
                    viewModel.deleteChat(chat)
                },
                userStatusMap = usersStatus.value
            )
            Button(
                onClick = {
                    createChatPreviewDataMocks(2)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Generate random chat")
            }
        }
    }
}
