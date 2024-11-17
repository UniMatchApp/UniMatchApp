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
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.DatePickerComponent
import com.ulpgc.uniMatch.ui.components.DropdownMenu
import com.ulpgc.uniMatch.ui.components.InputField
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun RegisterProfileScreen(
    userViewModel: UserViewModel,
    errorViewModel: ErrorViewModel,
    userId: String,
    onCompleteProfile: () -> Unit,
    location: Pair<Double, Double>?,
) {
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableIntStateOf(0) }
    var aboutMe by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.OTHER) }
    var sexualOrientation by remember { mutableStateOf(SexualOrientation.OTHER) }
    var relationshipType by remember { mutableStateOf(RelationshipType.FRIENDSHIP) }
    var birthday by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageUri: Uri? = data?.data
                selectedImageUri = imageUri
            }
        }

    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val REQUEST_CODE_READ_STORAGE = 1
    val permissionGranted = remember { mutableStateOf(false) }

    var showImageDialog by remember { mutableStateOf(false) }

    val profileCreated by userViewModel.profileCreated.collectAsState()

    val locationError = stringResource(R.string.location_error)
    val fieldsEmptyError = stringResource(R.string.fields_empty_error)
    val ageError = stringResource(R.string.age_error)

    LaunchedEffect(profileCreated) {
        if (profileCreated) {
            onCompleteProfile()
        }
    }

    LaunchedEffect(Unit) {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            permissionGranted.value = true
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_STORAGE)
        }
    }

    val permissionResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
        if (!granted) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_STORAGE)
        }
    }

    LaunchedEffect(permissionGranted.value) {
        if (!permissionGranted.value) {
            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    if (fullName.isEmpty() || aboutMe.isEmpty() || birthday.isEmpty() || selectedImageUri == null) {
                        errorViewModel.showError(fieldsEmptyError)
                    } else if (age < 18 || age > 100) {
                        errorViewModel.showError(ageError)
                    } else {
                        userViewModel.createProfile(
                            userId,
                            fullName,
                            age,
                            aboutMe,
                            gender,
                            sexualOrientation,
                            relationshipType,
                            birthday,
                            location,
                            selectedImageUri!!
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
}

