package com.ulpgc.uniMatch.ui.screens.core.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.event.EventSection
import com.ulpgc.uniMatch.ui.components.event.EventSurvey
import com.ulpgc.uniMatch.ui.screens.utils.DateParser
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper


@Composable
fun EventDetailScreen(
    eventId: String,
    eventViewModel: EventViewModel,
    profileViewModel: ProfileViewModel,
    onEventSurveyClick: (String) -> Unit
) {
    val event = eventViewModel.eventData.collectAsState().value
    val profile = profileViewModel.profileData.collectAsState().value

    LaunchedEffect(eventId) {
        eventViewModel.loadEvent(eventId)
    }

    event?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(event.attachment)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "User profile image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val formattedDate = DateParser.formatDateToString(it.date)
            val addressFromCoordinates = LocationHelper.getAddressFromCoordinates(it.location.latitude, it.location.longitude)

            val participantsText = it.participants.joinToString(", ")

            val fields = listOf(
                stringResource(R.string.event_title) to it.title,
                stringResource(R.string.event_date) to formattedDate,
                stringResource(R.string.event_location) to addressFromCoordinates,
                stringResource(R.string.event_members) to participantsText
            )

            fields.forEach { (label, value) ->
                EventSection(label = label, value = value)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Survey with 3 options and random votes in a map with a title
            EventSurvey("Will you go to the Yumbo?", options = mapOf("SIIII" to 10, "NO" to 25, "Antonio es Gay" to 15))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = { onEventSurveyClick(eventId) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.event_surveys), color = MaterialTheme.colorScheme.onBackground)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.participate), color = MaterialTheme.colorScheme.onBackground)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.likes.count().toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    val isLiked = event.likes.contains(profile?.userId ?: "")

                    IconButton(
                        onClick = { /*TODO: Lógica para dar like o quitar like*/ },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isLiked) "Liked" else "Not liked",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

        }
    }
}
