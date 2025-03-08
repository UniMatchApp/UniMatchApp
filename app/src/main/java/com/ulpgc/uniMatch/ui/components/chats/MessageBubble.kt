package com.ulpgc.uniMatch.ui.components.chats

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.models.Message
import com.ulpgc.uniMatch.ui.theme.Bone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    Log.d("MessageBubble", "timestamp: $timestamp -> formatTimestamp: ${sdf.format(date)}")
    return sdf.format(date)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: Message,
    isCurrentUser: Boolean,
    isSelected: Boolean,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { },
                onLongClick = { onLongClick() }
            ),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.8f)
                .background(
                    color = when {
                        isSelected -> Color(0xFF80DEEA)
                        isCurrentUser -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.tertiary
                    },
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
            ) {
                message.attachment?.let { attachment ->
                    Spacer(modifier = Modifier.height(8.dp))
                    val fileName = attachment.split('/').last()
                    val mimeType = context.contentResolver.getType(Uri.parse(attachment)) ?: "application/octet-stream"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = {},
                                onLongClick = { onLongClick() }
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            MessageBubbleContent(
                                mimeType = mimeType,
                                attachment = attachment,
                                isCurrentUser = isCurrentUser,
                                fileName = fileName,
                                context = context,
                                onLongClick = onLongClick
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (message.content.isNotBlank()) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTimestamp(message.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiary,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    MessageStatusIcon(message.receptionStatus)
                }
            }
        }
    }
}

@Composable
fun getFileIcon(mimeType: String): ImageVector {
    return when {
        mimeType.startsWith("image/") -> Icons.Default.Image
        mimeType.startsWith("audio/") -> Icons.Default.AudioFile
        mimeType.startsWith("video/") -> Icons.Default.VideoLibrary
        mimeType.startsWith("application/pdf") -> Icons.Default.PictureAsPdf
        mimeType.startsWith("application/msword") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") -> Icons.Default.Description
        else -> Icons.Default.AttachFile
    }
}

fun isFileDownloaded(attachment: String): Boolean {
    val file = File(attachment)
    return file.exists()
}

suspend fun downloadFile(
    attachment: String,
    fileName: String,
    context: Context,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        // Usar Coroutine para hacer la descarga en segundo plano
        withContext(Dispatchers.IO) {
            val uri = Uri.parse(attachment)
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val request = DownloadManager.Request(uri).apply {
                setTitle("Descargando archivo...")
                setDescription("Descargando: $fileName")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            }

            // Encolar la descarga y obtener el ID de la descarga
            val downloadId = downloadManager.enqueue(request)

            // Crear un BroadcastReceiver para recibir la finalización de la descarga
            val receiver = object : android.content.BroadcastReceiver() {
                override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
                    val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                    if (downloadId == id) {
                        // La descarga ha terminado, llamar a onSuccess
                        onSuccess()
                        context?.unregisterReceiver(this)  // Desregistrar el receptor
                    }
                }
            }

            // Registrar el receptor para recibir la finalización de la descarga
            val filter = android.content.IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    } catch (e: Exception) {
        Log.e("DownloadFile", "Error al iniciar la descarga: ${e.message}")
        onError("Error al descargar el archivo: ${e.message}")
    }
}

private fun downloadFileAsync(
    attachment: String,
    fileName: String,
    context: Context,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {}
) {
    CoroutineScope(Dispatchers.IO).launch {
        downloadFile(
            attachment = attachment,
            fileName = fileName,
            context = context,
            onSuccess = {
                onSuccess()
            },
            onError = {
                Log.e("DownloadFileAsync", "Error al descargar el archivo: $it")
                onError(it)
            }
        )
    }
}



@Composable
fun MessageStatusIcon(status: ReceptionStatus) {
    when (status) {
        ReceptionStatus.SENDING -> {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Sending",
                tint = Bone,
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.SENT -> {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Sent",
                tint = Bone,
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.RECEIVED -> {
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Received",
                tint = Bone,
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.READ -> {
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Read",
                tint = Color(0xFF0055FF),
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.FAILED -> {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Failed",
                tint = Color.Red,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun MessageBubbleContent(
    mimeType: String,
    attachment: String,
    isCurrentUser: Boolean,
    fileName: String,
    context: Context,
    onLongClick: () -> Unit
) {
    when {
        mimeType.startsWith("image/") -> {
            ImageAttachment(attachment = attachment, fileName = fileName, context = context)
        }
        mimeType.startsWith("video/") -> {
            VideoAttachment(attachment = attachment, fileName = fileName, context = context)
        }
        else -> {
            DefaultFileAttachment(mimeType = mimeType, fileName = fileName, attachmentUrl = attachment, context = context)
        }
    }
}

@Composable
fun ImageAttachment(
    attachment: String,
    fileName: String,
    context: Context
) {
    var isDownloading by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }

    // Comprobación inicial en un efecto de composición
    LaunchedEffect(attachment) {
        isDownloaded = isFileDownloaded(attachment)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isDownloaded) {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(attachment),
                contentDescription = "Image attachment",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        openFile(context, fileName, "image/*")
                    }
            )
        } else {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(attachment),
                contentDescription = "Image attachment",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .blur(8.dp) // Blur mientras se descarga
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Placeholder",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descargar imagen",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (!isDownloading) {
                    Button(
                        onClick = {
                            isDownloading = true

                            downloadFileAsync(
                                attachment = attachment,
                                fileName = fileName,
                                context = context,
                                onSuccess = {
                                    isDownloaded = true
                                    isDownloading = false
                                },
                                onError = {
                                    isDownloading = false
                                }
                            )
                        }
                    ) {
                        Text("Descargar")
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun VideoAttachment(
    attachment: String,
    fileName: String,
    context: Context
) {
    var isDownloading by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }

    // Comprobación inicial en un efecto de composición
    LaunchedEffect(attachment) {
        isDownloaded = isFileDownloaded(attachment)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isDownloaded) {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(attachment),
                contentDescription = "Video preview",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        openFile(context, fileName, "video/*")
                    }
            )
        } else {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(attachment),
                contentDescription = "Video preview",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .blur(8.dp) // Blur mientras se descarga
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = "Placeholder",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descargar video",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (!isDownloading) {
                    Button(
                        onClick = {
                            isDownloading = true
                            downloadFileAsync(
                                attachment = attachment,
                                fileName = fileName,
                                context = context,
                                onSuccess = {
                                    isDownloaded = true
                                    isDownloading = false
                                },
                                onError = {
                                    isDownloading = false
                                }
                            )
                        }
                    ) {
                        Text("Descargar")
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DefaultFileAttachment(
    mimeType: String,
    fileName: String,
    attachmentUrl: String,
    context: Context
) {
    var isDownloading by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }

    LaunchedEffect(attachmentUrl) {
        isDownloaded = File(context.filesDir, fileName).exists()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = getFileIcon(mimeType),
                contentDescription = "Attachment Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = fileName,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isDownloaded) {
                Button(
                    onClick = {
                        isDownloading = true
                        downloadFileAsync(
                            attachment = attachmentUrl,
                            fileName = fileName,
                            context = context,
                            onSuccess = {
                                isDownloaded = true
                                isDownloading = false
                            },
                            onError = {
                                isDownloading = false
                            }
                        )
                    }
                ) {
                    if (isDownloading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text("Descargar")
                    }
                }
            } else {
                Button(
                    onClick = { openFile(context, fileName, mimeType) }
                ) {
                    Text("Abrir")
                }
            }
        }
    }
}


fun openFile(context: Context, fileName: String, mimeType: String) {
    val file = when {
        mimeType.startsWith("video/") -> File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), fileName)
        mimeType.startsWith("image/") -> File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        else -> File(context.filesDir, fileName)
    }

    if (file.exists()) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "El archivo no existe", Toast.LENGTH_SHORT).show()
    }
}

