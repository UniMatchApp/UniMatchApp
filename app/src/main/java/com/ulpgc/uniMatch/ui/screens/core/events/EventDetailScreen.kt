package com.ulpgc.uniMatch.ui.screens.core.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ulpgc.uniMatch.R
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    eventViewModel: EventViewModel,
    userViewModel: UserViewModel
) {
    val event = eventViewModel.eventData.collectAsState().value

    LaunchedEffect(eventId) {
        eventViewModel.loadEvent(eventId)
    }

    event?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Event Image"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(it.date)

            val fields = listOf(
                stringResource(R.string.event_title) to it.title,
                stringResource(R.string.event_date) to formattedDate,
                stringResource(R.string.event_location) to it.location.toString()
            )

            fields.forEach { (label, value) ->
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(text = label, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.LightGray,
                            disabledBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        readOnly = true
                    )
                }
            }

            // Concatenar los miembros y mostrar en un solo OutlinedTextField
            val participantsText = it.participants.joinToString(", ")

            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(text = stringResource(R.string.event_members), style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = participantsText,  // Muestra todos los miembros juntos
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.LightGray,
                        disabledBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    readOnly = true
                )

            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.event_surveys), color = MaterialTheme.colorScheme.onPrimary)
                }

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.event_participants), color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
