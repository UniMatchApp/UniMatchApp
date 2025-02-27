package com.ulpgc.uniMatch.ui.screens.core.events

import LocationPicker
import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.event.EventDatePicker
import com.ulpgc.uniMatch.ui.components.event.EventSection
import com.ulpgc.uniMatch.ui.components.event.EventSurveyCard
import com.ulpgc.uniMatch.ui.theme.MainColor


@Composable
fun AddEventScreen(
    eventViewModel: EventViewModel,
    profileViewModel: ProfileViewModel,
) {
    var surveys by remember { mutableStateOf(mutableListOf<Survey>()) }
    var surveyCounter by remember { mutableStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? ComponentActivity

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri: Uri? = data?.data
            imageUri?.let {
                selectedImageUri = it
                eventViewModel.setUri(it)
            }
        }
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Tap to add image",
                    modifier = Modifier
                        .clickable { showDialog = true }
                        .padding(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        EventSection(
            label = stringResource(R.string.event_title),
            value = "Enter a title",
            readOnly = false,
            onValueChange = { eventViewModel.setTitle(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(8.dp))
        Column {
            Text(
                text = stringResource(R.string.event_location),
                color = MaterialTheme.colorScheme.onBackground
            )
            LocationPicker(
                onChangeLocation = { eventLocation ->
                    Log.i("AddEventScreen", "Location updated: $eventLocation")
                    eventLocation.latitude?.let {
                        eventLocation.longitude?.let { it1 ->
                            eventLocation.altitude?.let { it2 ->
                                eventViewModel.setLocation(it,
                                    it1, it2
                                )
                            }
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = stringResource(R.string.event_date),
                color = MaterialTheme.colorScheme.onBackground
            )
            EventDatePicker(
                onDateSelect = { date ->
                    Log.i("AddEventScreen", "Date updated: $date")
                    eventViewModel.setDateTime(date)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        surveys.forEach { surveyId ->
            EventSurveyCard(
                isEditing = true,
                onDeleteSurvey = {
                    surveys = surveys.filter {
                        it != surveyId
                    }.toMutableList()
                },
                onConfirmSurvey = { title, options ->
                    surveys = eventViewModel.createSurvey(surveys, title, options)
                },
                onVoteSurvey = { option ->

                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        ) {
            Button(
                onClick = {
                    surveys = ((surveys + surveyCounter) as MutableList<Survey>)
                    surveyCounter++
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.event_add_survey),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { eventViewModel.createEvent() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.event_complete),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.upload_image), color = MaterialTheme.colorScheme.onBackground) },
            text = { Text(stringResource(R.string.where_do_you_wanna_get_image)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        if (activity != null) {
                            ImagePicker.with(activity)
                                .cameraOnly()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .createIntent { intent -> imagePickerLauncher.launch(intent) }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.camera), color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        if (activity != null) {
                            ImagePicker.with(activity)
                                .galleryOnly()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .createIntent { intent -> imagePickerLauncher.launch(intent) }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.gallery), color = Color.White)
                }
            }
        )
    }
}



