package com.ulpgc.uniMatch.ui.screens.core.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.ui.components.chats.SearchBar


@Composable
fun EventsScreen(
    eventViewModel: EventViewModel,
    onEventClick: (String) -> Unit
) {

    val isSearchActive = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        eventViewModel.loadEvents()
    }

    val events = eventViewModel.eventsData.collectAsState().value

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .weight(1f)
            ) {
                SearchBar(
                    searchText = searchText.value,
                    onSearchTextChange = {
                        searchText.value = it
                    },
                    onArrowBackCallback = {
                        isSearchActive.value = false
                        searchText.value = ""
                    },
                    backgroundColor = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),) {
                Text(text = stringResource(R.string.add_event), color = Color.White)
            }
        }

        EventsList(
            events = events,
            onEventClick = { event ->
                onEventClick(event.eventId)
            }
        )
    }
}




@Composable
fun EventsList(
    events: List<Event>? = emptyList(),
    onEventClick: (Event) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Usamos 'items' para iterar sobre la lista de eventos
        items(events ?: emptyList()) { event ->
            EventCard(
                event = event,
                onEventClick = { onEventClick(event) }
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre las tarjetas
        }
    }
}
@Composable
fun EventCard(
    event: Event,
    onEventClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEventClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(event.attachment)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = "Event image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()  // Ensures the image covers the entire card
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(event.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(event.location.toString(), fontSize = 14.sp, color = Color.Gray)
                Text(event.date.toString(), fontSize = 14.sp, color = Color.White)
            }
        }
    }
}
