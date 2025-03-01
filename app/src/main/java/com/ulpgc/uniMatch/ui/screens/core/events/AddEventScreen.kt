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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.event.EventDatePicker
import com.ulpgc.uniMatch.ui.components.event.EventSection
import com.ulpgc.uniMatch.ui.components.event.EventSurveyCard
import com.ulpgc.uniMatch.ui.theme.MainColor
import kotlinx.coroutines.launch


@Composable
fun AddEventScreen(
    eventViewModel: EventViewModel,
    errorViewModel: ErrorViewModel,
    profileViewModel: ProfileViewModel,
) {

    val coroutineScope = rememberCoroutineScope()



    var showDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? ComponentActivity

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var eventToCreate = eventViewModel.eventCreated.collectAsState().value
    var surveys by remember { mutableStateOf(eventToCreate.surveys) }

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

        var titleText by remember { mutableStateOf(eventToCreate?.title) }

        EventSection(
            label = stringResource(R.string.event_title),
            value = titleText ?: "Title",
            readOnly = false,
            onValueChange = { newText ->
                titleText = newText
                eventViewModel.setTitle(newText)
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = stringResource(R.string.event_location),
                color = MaterialTheme.colorScheme.onBackground
            )
            LocationPicker(
                onChangeLocation = { eventLocation ->
                    eventLocation.apply {
                        if (latitude != null && longitude != null && altitude != null) {
                            eventViewModel.setLocation(latitude!!, longitude!!, altitude!!)
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
                    eventViewModel.setDateTime(date)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        surveys?.forEach { survey ->
            EventSurveyCard(
                isEditing = true,
                onDeleteSurvey = {
                    surveys = surveys?.filter {
                        it != survey
                    }?.toMutableList()
                },
                onConfirmSurvey = { title, options ->
                    eventViewModel.createSurvey(title, options)
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

                    Log.i("AddEventScreen", "Encuesta añadida $surveys")
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
                onClick = {
                    coroutineScope.launch {
                        val result = eventViewModel.createEvent()
                        result.onSuccess {
                            Log.i("AddEventScreen", "Evento creado correctamente")
                        }.onFailure { error ->
                            errorViewModel.showError(error.message ?: "Ocurrió un error inesperado")
                        }
                    }
                },
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



