package com.ulpgc.uniMatch.ui.components.chats

// Función auxiliar para formatear el timestamp

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.domain.enums.ReceptionStatus
import com.ulpgc.uniMatch.data.domain.models.Message

import com.ulpgc.uniMatch.ui.theme.Bone
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Función auxiliar para formatear el timestamp
fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()) // Usa el locale del dispositivo
    // Print the timestamp in the format "HH:mm"
    Log.d("MessageBubble", "timestamp: $timestamp -> formatTimestamp: ${sdf.format(date)}")
    return sdf.format(date)
}

@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
    Log.d("MessageBubble", "MessageBubble: $message")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth() // El ancho de la burbuja se ajusta al contenido
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.8f) // Limita el ancho de la burbuja al 80% del ancho de la pantalla
                .background(
                    color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
            ) {
                Text(
                    text = message.content,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Sección del timestamp y estado del mensaje
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTimestamp(message.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onTertiary,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // Mostrar el ícono basado en el estado del mensaje
                    if (isCurrentUser) {
                        MessageStatusIcon(message.receptionStatus)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageStatusIcon(status: ReceptionStatus) {
    when (status) {
        ReceptionStatus.SENDING -> {
            Icon(
                imageVector = Icons.Default.Schedule, // Icono de "Enviando"
                contentDescription = "Sending",
                tint = Bone,
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.SENT -> {
            Icon(
                imageVector = Icons.Default.Check, // Icono de "Enviado"
                contentDescription = "Sent",
                tint = Bone,
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.RECEIVED -> {
            Icon(
                imageVector = Icons.Default.DoneAll, // Icono de "Recibido"
                contentDescription = "Received",
                tint = Bone,
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.READ -> {
            Icon(
                imageVector = Icons.Default.DoneAll, // Icono de "Leído"
                contentDescription = "Read",
                tint = Color(0xFF0055FF), // Color azul para leído
                modifier = Modifier.size(16.dp)
            )
        }

        ReceptionStatus.FAILED -> {
            Icon(
                imageVector = Icons.Default.Close, // Icono de "Fallido"
                contentDescription = "Failed",
                tint = Color.Red,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


