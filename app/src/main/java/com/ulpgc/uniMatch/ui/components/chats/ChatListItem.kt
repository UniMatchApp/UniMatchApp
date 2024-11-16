import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ulpgc.uniMatch.R

@Composable
fun ChatListItem(
    profileImageUrl: String?,  // URL de la imagen del perfil
    userName: String,
    lastMessage: String,
    lastMessageTime: String,
    unreadMessagesCount: Int,
    onChatClick: () -> Unit
) {
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
        // Imagen de perfil
        Image(
            painter = painter,
            contentDescription = "$userName profile picture",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Información del chat
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lastMessage,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onTertiary,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Hora del último mensaje y contador de mensajes no leídos
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = lastMessageTime,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Contador de mensajes no leídos
            if (unreadMessagesCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp) // Tamaño fijo para asegurar la forma circular
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = unreadMessagesCount.toString(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold, // Opcional: para mejor visibilidad
                        modifier = Modifier.align(Alignment.Center) // Center the Text
                    )
                }
            }
        }
    }
}
