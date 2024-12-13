package com.ulpgc.uniMatch.ui.screens.auth.register

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.ulpgc.uniMatch.data.domain.enums.Facts
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.DatePickerComponent
import com.ulpgc.uniMatch.ui.components.DropdownMenu
import com.ulpgc.uniMatch.ui.components.InputField
import com.ulpgc.uniMatch.ui.screens.utils.stringToEnum
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun RegisterProfileScreen(
    userViewModel: UserViewModel,
    errorViewModel: ErrorViewModel,
    userId: String,
    onCompleteProfile: () -> Unit,
    onBack: () -> Unit,
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

    val context = LocalContext.current

    val genderMap =
        context.resources.getStringArray(R.array.genders).mapIndexed { index, name ->
            Gender.entries[index] to name
        }.toMap()

    val sexualOrientationMap =
        context.resources.getStringArray(R.array.sexual_orientation).mapIndexed { index, name ->
            SexualOrientation.entries[index] to name
        }.toMap()

    val relationshipTypeMap =
        context.resources.getStringArray(R.array.relationship_type).mapIndexed { index, name ->
            RelationshipType.entries[index] to name
        }.toMap()

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageUri: Uri? = data?.data
                selectedImageUri = imageUri
            }
        }

    val activity = LocalContext.current as Activity
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

    BackHandler { onBack() }

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
        val patterns = listOf("d/M/yyyy", "dd/MM/yyyy")
        val formatter = patterns
            .map { DateTimeFormatter.ofPattern(it) }
            .firstNotNullOfOrNull { runCatching { LocalDate.parse(birthday, it) }.getOrNull() }

        val currentDate = LocalDate.now()
        return Period.between(formatter, currentDate).years
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
                },
                color = MaterialTheme.colorScheme.primary
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
                items = genderMap.values.toList(),
                selectedItem = gender.name,
                onItemSelected = { selected ->
                    genderMap.entries.find { it.value == selected }?.key?.let {
                        gender = it
                    }
                },
                includeNullOption = false
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.sexual_orientation))
            DropdownMenu(
                items = sexualOrientationMap.values.toList(),
                selectedItem = sexualOrientation.name,
                onItemSelected = { selected ->
                    sexualOrientationMap.entries.find { it.value == selected }?.key?.let {
                        sexualOrientation = it
                    }
                },
                includeNullOption = false
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.relationship_type_title))
            DropdownMenu(
                items = relationshipTypeMap.values.toList(),
                selectedItem = relationshipType.name,
                onItemSelected = { selected ->
                    relationshipTypeMap.entries.find { it.value == selected }?.key?.let {
                        relationshipType = it
                    }
                },
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
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.select_image), color = Color.White)
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
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.complete_profile), color = Color.White)
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

