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
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.event.EventSection
import com.ulpgc.uniMatch.ui.screens.utils.DateParser
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper


@Composable
fun EventDetailScreen(
    eventId: String,
    eventViewModel: EventViewModel,
    profileViewModel: ProfileViewModel,
    userViewModel: UserViewModel,
    onEventSurveyClick: (String) -> Unit
) {
    val event = eventViewModel.eventData.collectAsState().value

    LaunchedEffect(eventId) {
        eventViewModel.loadEvent(eventId)

    }

    event?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(event.attachment)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "Event profile image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val formattedDate = DateParser.formatDateToString(it.date)
            val addressFromCoordinates = LocationHelper.getAddressFromCoordinates(
                it.location?.latitude,
                it.location?.longitude
            )

            val participantsText = it.participants.joinToString(", ")

            val fields = listOf(
                stringResource(R.string.event_title) to it.title,
                stringResource(R.string.event_date) to formattedDate,
                stringResource(R.string.event_location) to addressFromCoordinates,
                stringResource(R.string.event_members) to participantsText
            )

            fields.forEach { (label, value) ->
                EventSection(
                    label = label,
                    value = value,
                    isLocation = label == stringResource(R.string.event_location)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onEventSurveyClick(eventId) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1.7f)
                ) {
                    Text(text = stringResource(R.string.event_surveys), color = MaterialTheme.colorScheme.onBackground)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (event.participants.contains(userViewModel.userId)) {
                            eventViewModel.removeParticipation(eventId)
                        } else {
                            eventViewModel.addParticipation(eventId)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1.7f)
                ) {
                    Text(
                        text = if (event.participants.contains(userViewModel.userId)) {
                            stringResource(R.string.remove_participation)
                        } else {
                            stringResource(R.string.participate)
                        },
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                    .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = event.likes.count().toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    val isLiked = event.likes.contains(userViewModel.userId)

                    IconButton(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape),
                        onClick = {
                            if (isLiked) {
                                eventViewModel.dislikeEvent(eventId)
                            } else {
                                eventViewModel.likeEvent(eventId)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isLiked) "Liked" else "Not liked",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.01f))
        }
    }
}
