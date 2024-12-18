package com.ulpgc.uniMatch.ui.screens.core.notifications

import AppNotificationPayload
import EventNotificationPayload
import MatchNotificationPayload
import MessageNotificationPayload
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.models.notification.Notification
import com.ulpgc.uniMatch.data.infrastructure.viewModels.NotificationsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationsScreen(
    notificationsViewModel: NotificationsViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {
    val notifications by notificationsViewModel.notifications.collectAsState()
    val notificationsEnabled by notificationsViewModel.notificationsEnabled.collectAsState()
    val profiles by profileViewModel.profiles.collectAsState()
    val isLoading by notificationsViewModel.isLoading.collectAsState()
    val profileLoading by profileViewModel.isLoading.collectAsState()
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val deleteAll = remember { mutableStateOf(false) }
    val notificationToDelete = remember { mutableStateOf<Notification?>(null) }

    LaunchedEffect(Unit) {
        notificationsViewModel.loadNotifications()
    }

    LaunchedEffect(notifications) {
        notifications.forEach { notification ->
            notificationsViewModel.markNotificationAsRead(notification.id)
            val payload = notification.payload
            if (payload is MatchNotificationPayload) {
                profileViewModel.getProfileInfo(payload.getUserMatched())
            } else if (payload is MessageNotificationPayload) {
                profileViewModel.getProfileInfo(payload.getSender())
            }
        }
    }

    if (showDeleteConfirmationDialog.value) {
        DeleteConfirmationDialog(
            onConfirm = {
                if (deleteAll.value) {
                    notificationsViewModel.deleteAllNotifications()
                } else {
                    notificationToDelete.value?.let {
                        notificationsViewModel.deleteNotification(it.id)
                    }
                }
                showDeleteConfirmationDialog.value = false
                deleteAll.value = false
            },
            onDismiss = {
                showDeleteConfirmationDialog.value = false
                deleteAll.value = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.notifications_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsViewModel.toggleNotifications(it)
                },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isLoading && !profileLoading) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(notifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        date = notification.date,
                        profiles = profiles,
                        onDeleteNotification = { deletedNotification ->
                            notificationToDelete.value = deletedNotification
                            deleteAll.value = false
                            showDeleteConfirmationDialog.value = true
                        },
                        navController = navController
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                deleteAll.value = true
                showDeleteConfirmationDialog.value = true
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_all_notifications),
                tint = Color.White
            )
        }
    }

    if (isLoading || profileLoading) {
        LoadingSkeleton()
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.delete_confirmation))
        },
        text = {
            Text(text = stringResource(R.string.delete_notification_confirmation))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun LoadingSkeleton() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(5) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.width(100.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    navController: NavController,
    date: Long,
    profiles : List<Profile>,
    onDeleteNotification: (Notification) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Log.i("NotificationCard", "NotificationCard: $notification")
                notification.payload.let {
                    when (it) {
                        is AppNotificationPayload -> {
                            Text(
                                text = it.getTitle(),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = it.getDescription(),
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2
                            )
                            DateWidget(date, notification, onDeleteNotification)
                        }

                        is EventNotificationPayload -> {
                            Text(
                                text = it.getTitle(),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Status: ${it.getStatus()}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            DateWidget(date, notification, onDeleteNotification)
                        }

                        is MatchNotificationPayload -> {
                            val profile = profiles.find { profile -> profile.userId == it.getUserMatched() }

                            if (profile != null) {
                                Text(
                                    text = if (it.isLiked()) "${profile.name} ${stringResource(R.string.liked_you)}"
                                    else "${profile.name} ${stringResource(R.string.unliked_you)}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                DateWidget(date, notification, onDeleteNotification)
                            }
                        }

                        is MessageNotificationPayload -> {
                            val profile = profiles.find { profile -> profile.userId == it.getSender() }

                            if (profile != null) {
                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Navegar a la pantalla del chat
                                            navController.navigate("chatDetail/${it.getSender()}")
                                        },
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    profile.preferredImage.let { image ->
                                        Image(
                                            painter = rememberImagePainter(image),
                                            contentDescription = "Profile image",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color.Gray),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Column {
                                        Text(
                                            text = "${stringResource(R.string.you_have_a_new_message_from)} ${profile.name}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = it.getContent(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 2
                                        )
                                        it.getThumbnail()?.let {
                                            Text(
                                                text = stringResource(R.string.thumbnail_available),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }

                                DateWidget(date, notification, onDeleteNotification)
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun DateWidget(
    date: Long,
    notification: Notification,
    onDeleteNotification: (Notification) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatDate(date),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )

        IconButton(
            onClick = { onDeleteNotification(notification) },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete notification")
        }
    }
}


private fun formatDate(date: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(date))
}

