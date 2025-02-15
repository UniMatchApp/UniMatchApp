package com.ulpgc.uniMatch.ui.screens.core.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel

@Composable
fun EventDetailScreen (
    eventId: String,
    eventViewModel: EventViewModel,
    userViewModel: UserViewModel
) {

    val event = eventViewModel.eventData.collectAsState().value

    LaunchedEffect(eventId) {
        eventViewModel.loadEvent(eventId)
    }

    event?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title of the event
            Text(text = "Event Title: ${it.title}")

            // List of participants
            Text(text = "Participants: ${it.participants.joinToString()}")
        }
    }





}