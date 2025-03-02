package com.ulpgc.uniMatch.ui.screens.core.chat

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.chats.MessageBubble
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ChatDetailScreen(
    chatId: String,
    chatViewModel: ChatViewModel,
    userViewModel: UserViewModel,
    context: Context
) {
    val messages by chatViewModel.messages.observeAsState(emptyList())
    val filteredMessages = messages.filter { it.senderId == chatId || it.recipientId == chatId }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val selectedAttachment by chatViewModel.selectedAttachment.observeAsState(null)

    var showOptionsMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedMessage: Message? by remember { mutableStateOf(null) }
    var selectedMessageOffset by remember { mutableStateOf(0f) }

    LaunchedEffect(chatId) {
        chatViewModel.loadMessages(chatId, filteredMessages.size)
    }

    LaunchedEffect(filteredMessages) {
        chatViewModel.setMessagesAsRead(filteredMessages)
    }

    val isFarFromBottom = remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= filteredMessages.size - 8
        }
    }

    val showScrollToBottomButton by remember {
        derivedStateOf {
            isFarFromBottom.value && filteredMessages.isNotEmpty()
        }
    }

    LaunchedEffect(filteredMessages.size) {
        if (isFarFromBottom.value && filteredMessages.isNotEmpty()) {
            listState.animateScrollToItem(filteredMessages.size - 1)
        }
    }

    // Detectar el toque fuera del diálogo para cerrarlo
    Box(
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures(onPress = {
                if (showOptionsMenu || showDeleteDialog) {
                    showOptionsMenu = false
                    showDeleteDialog = false
                    selectedMessage = null
                }
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            if (showOptionsMenu && selectedMessage != null) {
                OptionsMenu(
                    chatViewModel,
                    selectedMessage!!,
                    onDeleteClicked = {
                        showDeleteDialog = true
                        showOptionsMenu = false
                    },
                    onCancelClicked = {
                        showOptionsMenu = false
                        selectedMessage = null
                    }
                )
            }

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
                        .weight(1f)
                ) {
                    itemsIndexed(filteredMessages) { index, message ->
                        MessageBubble(
                            message = message,
                            isCurrentUser = message.senderId == userViewModel.userId,
                            isSelected = selectedMessage == message,
                            onLongClick = {
                                selectedMessage = message
                                coroutineScope.launch {
                                    listState.layoutInfo.visibleItemsInfo.find { it.index == index }?.let {
                                        selectedMessageOffset = it.offset.toFloat()
                                        showOptionsMenu = true
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (filteredMessages.isNotEmpty() && !showScrollToBottomButton) {
                    ScrollToBottomButton(
                        onClick = {
                            coroutineScope.launch {
                                if (filteredMessages.isNotEmpty()) {
                                    listState.animateScrollToItem(filteredMessages.size - 1)
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

            AttachmentPreview(
                attachment = selectedAttachment,
                onRemoveAttachment = { chatViewModel.removeAttachment() },
                modifier = Modifier.fillMaxWidth()
            )

            MessageInput(
                viewModel = chatViewModel,
                chatId = chatId,
                modifier = Modifier.fillMaxWidth(),
                onTyping = { typing -> if (typing) userViewModel.userId?.let { chatViewModel.setUserTyping(chatId) } else chatViewModel.setUserStoppedTyping() },
                onStoppedTyping = { chatViewModel.setUserStoppedTyping() },
                onClick = {
                    if (showOptionsMenu || showDeleteDialog) {
                        showOptionsMenu = false
                        showDeleteDialog = false
                        selectedMessage = null
                    }
                },
                messageToEdit = selectedMessage,
                context = context
            )
        }

        if (showDeleteDialog && selectedMessage != null) {
            DeleteMessageAlertDialog(
                onConfirm = {
                    chatViewModel.deleteMessage(selectedMessage!!.messageId)
                    showDeleteDialog = false
                    selectedMessage = null
                },
                onDismiss = { showDeleteDialog = false; selectedMessage = null }
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
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Scroll to bottom",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun AttachmentPreview(
    attachment: String?,
    onRemoveAttachment: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (attachment != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(MaterialTheme.shapes.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val file = File(attachment)
            val fileExtension = file.extension.lowercase()

            // Verificamos si el archivo es una imagen por su extensión
            if (fileExtension in listOf("jpg", "jpeg", "png", "gif")) {
                Image(
                    painter = rememberAsyncImagePainter(attachment),
                    contentDescription = "Image preview",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.background)
                )
            } else {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = fileExtension.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onRemoveAttachment,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = CircleShape
                    )
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove attachment",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}



@Composable
fun MessageInput(
    viewModel: ChatViewModel,
    chatId: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    context: Context,
    messageToEdit: Message? = null,
    onTyping: (Boolean) -> Unit,
    onStoppedTyping: () -> Unit
) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()
    var typingJob: Job? by remember { mutableStateOf(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val filePath = it.toString()
                viewModel.setSelectedAttachment(filePath)
            }
        }
    )

    LaunchedEffect(messageToEdit) {
        if (messageToEdit?.let { viewModel.isMessageEditable(it) } == true) {
            inputText = TextFieldValue(messageToEdit.content) ?: TextFieldValue("")
        }
    }

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = inputText,
            onValueChange = {
                inputText = it
                onTyping(it.text.isNotEmpty())
                if (it.text.isNotEmpty()) {
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
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clip(CircleShape),
            placeholder = { Text("Write a message...") },
            leadingIcon = {
                if (messageToEdit == null || !viewModel.isMessageEditable(messageToEdit)) {
                    IconButton(
                        onClick = {
                            filePickerLauncher.launch("*/*")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Attachment,
                            contentDescription = "Attach file",
                            modifier = Modifier
                                .size(24.dp)
                                .pointerInput(Unit) {
                                    onClick()
                                },
                        )
                    }
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (inputText.text.isNotEmpty()) {
                        if (messageToEdit == null || !viewModel.isMessageEditable(messageToEdit)) {
                            viewModel.sendMessage(chatId, inputText.text)
                        } else {
                            viewModel.editMessage(messageToEdit.messageId, inputText.text)
                        }
                        inputText = TextFieldValue("")
                    }
                    onClick()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        )
    }
}

@Composable
fun OptionsMenu(
    chatViewModel: ChatViewModel,
    message: Message,
    onDeleteClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(MaterialTheme.shapes.small),
        horizontalArrangement = Arrangement.End
    ) {
        if (chatViewModel.isMessageDeletable(message)) {
            TextButton(onClick = onDeleteClicked) {
                Text("Eliminar")
            }
        }
        TextButton(onClick = onCancelClicked) {
            Text("Cancelar")
        }
    }
}


@Composable
fun DeleteMessageAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar eliminación") },
        text = { Text("¿Estás seguro de que deseas eliminar este mensaje?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


