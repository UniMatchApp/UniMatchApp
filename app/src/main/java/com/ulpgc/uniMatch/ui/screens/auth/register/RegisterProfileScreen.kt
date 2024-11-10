package com.ulpgc.uniMatch.ui.screens.auth.register

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.components.DatePickerComponent
import com.ulpgc.uniMatch.ui.components.DropdownMenu
import com.ulpgc.uniMatch.ui.components.InputField
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun RegisterProfileScreen(
    authViewModel: AuthViewModel,
    userId: String,
    onCompleteProfile: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableIntStateOf(0) }
    var aboutMe by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.OTHER) }
    var sexualOrientation by remember { mutableStateOf(SexualOrientation.OTHER) }
    var relationshipType by remember { mutableStateOf(RelationshipType.FRIENDSHIP) }
    var birthday by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableIntStateOf(0) }
    val PERMISSION_REQUEST_CODE = 1001
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageUri: Uri? = data?.data
                selectedImageUri = imageUri
            }
        }

    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    var showImageDialog by remember { mutableStateOf(false) }

    val profileCreated by authViewModel.profileCreated.collectAsState()

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            location = LocationHelper.getCurrentLocation(context)
            if (location == null) {
                showErrorDialog = true
                errorMessage = R.string.location_error
            }
        }
    }

    LaunchedEffect(profileCreated) {
        if (profileCreated) {
            onCompleteProfile()
        }
    }

    fun calculateAge(birthday: String): Int {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val birthDate = LocalDate.parse(birthday, formatter)
        val currentDate = LocalDate.now()
        return Period.between(birthDate, currentDate).years
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.unimatch_logo),
                    contentDescription = "Unimatch Logo",
                    modifier = Modifier.size(125.dp)
                )
                Text(
                    text = stringResource(R.string.complete_profile),
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.full_name))
            InputField(
                value = fullName,
                onValueChange = { fullName = it },
                label = stringResource(R.string.full_name)
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.birthday))
            DatePickerComponent(
                selectedDate = birthday,
                onDateSelected = {
                    birthday = it
                    age = calculateAge(it)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${stringResource(R.string.age)}: $age")
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.about_me))
            InputField(
                value = aboutMe,
                onValueChange = { aboutMe = it },
                label = stringResource(R.string.about_me)
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.gender))
            DropdownMenu(
                items = Gender.entries.map { it.name },
                selectedItem = gender.name,
                onItemSelected = { gender = Gender.valueOf(it ?: Gender.OTHER.name) },
                includeNullOption = false
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.sexual_orientation))
            DropdownMenu(
                items = SexualOrientation.entries.map { it.name },
                selectedItem = sexualOrientation.name,
                onItemSelected = { sexualOrientation = SexualOrientation.valueOf(it ?: SexualOrientation.OTHER.name) },
                includeNullOption = false
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.relationship_type_title))
            DropdownMenu(
                items = RelationshipType.entries.map { it.name },
                selectedItem = relationshipType.name,
                onItemSelected = { relationshipType = RelationshipType.valueOf(it ?: RelationshipType.FRIENDSHIP.name) },
                includeNullOption = false
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.select_image))
            Button(
                onClick = {
                    ImagePicker.with(activity)
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent -> imagePickerLauncher.launch(intent) }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.select_image))
            }

            selectedImageUri?.let { uri ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.selected_image))
                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image Preview",
                        modifier = Modifier
                            .sizeIn(maxWidth = 150.dp, maxHeight = 150.dp)
                            .clickable { showImageDialog = true }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (location == null) {
                        showErrorDialog = true
                        errorMessage = R.string.location_error
                    }
                    else if (fullName.isEmpty() || aboutMe.isEmpty() || birthday.isEmpty() || selectedImageUri == null || age < 18) {
                        showErrorDialog = true
                        errorMessage = R.string.fields_empty_error
                    } else {
                        authViewModel.createProfile(
                            userId,
                            fullName,
                            age,
                            aboutMe,
                            gender,
                            sexualOrientation,
                            relationshipType,
                            birthday,
                            location,
                            selectedImageUri.toString()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.complete_profile))
            }
        }
    }

    if (showImageDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { showImageDialog = false },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { showImageDialog = false },
                color = Color.Transparent
            ) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Image in Dialog",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(stringResource(R.string.error_title)) },
            text = { Text(stringResource(errorMessage)) },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

