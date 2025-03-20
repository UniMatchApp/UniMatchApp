package com.ulpgc.uniMatch.ui.components.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enums.ChatStatus

@Composable
fun ChatListItem(
    profileImageUrl: String?,
    userName: String,
    lastMessage: String,
    lastMessageTime: String,
    unreadMessagesCount: Int,
    onChatClick: () -> Unit,
    onChatDelete: () -> Unit,
    userStatus: ChatStatus?
) {
    var showDialog by remember { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(
        model = profileImageUrl ?: R.drawable.icon_user_filled,
        placeholder = painterResource(R.drawable.icon_user_filled),
        error = painterResource(R.drawable.icon_user_filled)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onChatClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Image(
                painter = painter,
                contentDescription = "$userName profile picture",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )

            userStatus?.let {
                val color = when (it) {
                    ChatStatus.ONLINE -> Color.Green
                    ChatStatus.OFFLINE -> Color.Gray
                    ChatStatus.TYPING -> Color.Green
                }
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color, CircleShape)
                        .align(Alignment.BottomEnd)
                        .padding(2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (userStatus == ChatStatus.TYPING) {
                    stringResource(R.string.typing)
                } else {
                    lastMessage
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (userStatus == ChatStatus.TYPING) Color.Green else MaterialTheme.colorScheme.onTertiary,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = lastMessageTime,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (unreadMessagesCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = unreadMessagesCount.toString(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { showDialog = true }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_chat),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete_chat_confirmation)) },
            text = { Text(stringResource(R.string.confirm_delete_chat)) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onChatDelete()
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
