package com.ulpgc.uniMatch.ui.screens.core.events
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.ui.components.chats.SearchBar

@Composable
fun EventsScreen(
    eventViewModel: EventViewModel
) {
    val isSearchActive = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        eventViewModel.loadEvents()
    }

    val events = eventViewModel.eventsData.collectAsState().value

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row() {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            ) {
                Text(text = stringResource(R.string.add_event), color = Color.White)
            }
        }

        // Pass the events data to the EventsList composable
        EventsList(events = events)
    }
}

@Composable
fun EventsList(events: List<Event>? = emptyList()) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        events?.forEach { event ->
            EventCard(event = event)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
//            Image(painter = painterResource(id = R.drawable.event_placeholder), contentDescription = "Event Image")
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(event.location.toString(), fontSize = 14.sp, color = Color.Gray)
            Text(event.date.toString(), fontSize = 14.sp)
        }
    }
}
