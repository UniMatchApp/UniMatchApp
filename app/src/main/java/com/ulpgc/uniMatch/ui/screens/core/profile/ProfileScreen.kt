package com.ulpgc.uniMatch.ui.screens.core.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ulpgc.uniMatch.data.domain.enums.Education
import com.ulpgc.uniMatch.data.domain.enums.Facts
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.Interests
import com.ulpgc.uniMatch.data.domain.enums.Jobs
import com.ulpgc.uniMatch.data.domain.enums.Personality
import com.ulpgc.uniMatch.data.domain.enums.Pets
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.DropdownMenu
import com.ulpgc.uniMatch.ui.components.profile.LegalSection
import com.ulpgc.uniMatch.ui.components.profile.ProfileDropdownField
import com.ulpgc.uniMatch.ui.components.profile.ProfileInputField
import com.ulpgc.uniMatch.ui.components.profile.ProfileSection
import com.ulpgc.uniMatch.ui.screens.utils.enumToString
import com.ulpgc.uniMatch.ui.screens.utils.stringToEnum

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onEditClick: (String) -> Unit,
    onEditInterestsClick: (String) -> Unit,
    onCookiesClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val context = LocalContext.current

    val profile = profileViewModel.profileData.collectAsState().value

    val isLoading by profileViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (profile != null) {

        var aboutMeText by remember { mutableStateOf(profile.aboutMe ?: "") }

        Log.i("ProfileScreen", "Profile: $profile")

        val horoscopeMap =
            context.resources.getStringArray(R.array.horoscope).mapIndexed { index, name ->
                Horoscope.entries.getOrNull(index) to name
            }.toMap()

        val relationshipTypeMap =
            context.resources.getStringArray(R.array.relationship_type).mapIndexed { index, name ->
                RelationshipType.entries[index] to name
            }.toMap()

        val genderMap =
            context.resources.getStringArray(R.array.genders).mapIndexed { index, name ->
                Gender.entries[index] to name
            }.toMap()

        val sexualOrientationMap =
            context.resources.getStringArray(R.array.sexual_orientation).mapIndexed { index, name ->
                SexualOrientation.entries[index] to name
            }.toMap()

        val religionMap =
            context.resources.getStringArray(R.array.religion).mapIndexed { index, name ->
                Religion.entries.getOrNull(index) to name
            }.toMap()

        val habitsMap = context.resources.getStringArray(R.array.habits).mapIndexed { index, name ->
            Habits.entries.getOrNull(index) to name
        }.toMap()

        val jobsMap = context.resources.getStringArray(R.array.jobs).mapIndexed { index, name ->
            Jobs.entries.getOrNull(index) to name
        }.toMap()

        val petsMap = context.resources.getStringArray(R.array.pets).mapIndexed { index, name ->
            Pets.entries.getOrNull(index) to name
        }.toMap()

        val personalityMap =
            context.resources.getStringArray(R.array.personality_type).mapIndexed { index, name ->
                Personality.entries.getOrNull(index) to name
            }.toMap()

        val educationMap =
            context.resources.getStringArray(R.array.education).mapIndexed { index, name ->
                Education.entries.getOrNull(index) to name
            }.toMap()

        val factsMap = context.resources.getStringArray(R.array.facts).mapIndexed { index, name ->
            Facts.entries.getOrNull(index) to name
        }.toMap()

        val interestsMap =
            context.resources.getStringArray(R.array.interests).mapIndexed { index, name ->
                Interests.entries.getOrNull(index) to name
            }.toMap()

        var profileInterests = interestsMap.mapNotNull { entry ->
            if (profile.interestsList.contains(enumToString(entry.key))) {
                entry.value
            } else {
                null
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(profile.preferredImage)
                                .build()
                        )

                        Image(
                            painter = painter,
                            contentDescription = "User profile image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White, CircleShape)
                                .border(2.dp, Color.Black, CircleShape)
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            IconButton(onClick = { profile.let { onEditClick(it.profileId) } }) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_edit),
                                    contentDescription = "Edit profile",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${profile.name ?: "Nombre no disponible"}, ${profile.age ?: "--"}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.about_me),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = aboutMeText,
                onValueChange = { newText ->
                    aboutMeText = newText
                    profile.aboutMe = newText
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.questions),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )

            DropdownMenu(
                items = factsMap.values.toList(),
                selectedItem = factsMap[stringToEnum<Facts>(profile.fact)],
                onItemSelected = { newFact ->
                    val newFact = factsMap.entries.find { it.value == newFact }?.key
                    if (newFact != null) {
                        profile.fact = enumToString(newFact)
                    }
                },
                includeNullOption = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.interests),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { profile.let { onEditInterestsClick(it.profileId) } }
                    .border(width = 1.dp, color = Color.Gray)
                    .padding(16.dp)
            ) {
                Text(
                    text = profileInterests.joinToString(", "),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProfileDropdownField(
                label = stringResource(R.string.gender),
                options = context.resources.getStringArray(R.array.genders).toList(),
                selectedOption = genderMap[profile.genderEnum]!!,
                onEditField = { selectedOption ->
                    var genderOption = genderMap.entries.find { it.value == selectedOption }?.key
                    if (genderOption != null) {
                        profile.gender = genderOption.toString()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInputField(
                label = stringResource(R.string.height),
                initialValue = profile.height.toString(),
                onValueChange = { newHeight ->
                    profile.height = newHeight.toIntOrNull() ?: 170
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInputField(
                label = stringResource(R.string.weight),
                initialValue = profile.weight.toString(),
                onValueChange = { newWeight ->
                    profile.weight = newWeight.toIntOrNull() ?: 70
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileDropdownField(
                label = stringResource(R.string.sexual_orientation),
                options = context.resources.getStringArray(R.array.sexual_orientation).toList(),
                selectedOption = sexualOrientationMap[profile.sexualOrientationEnum]!!,
                onEditField = { selectedOption ->
                    val sexualOrientationOption =
                        sexualOrientationMap.entries.find { it.value == selectedOption }?.key
                    if (sexualOrientationOption != null) {
                        profile.sexualOrientation = sexualOrientationOption.toString()
                    }
                },
                includeNullOption = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileDropdownField(
                label = stringResource(R.string.job),
                options = jobsMap.values.toList(),
                selectedOption = jobsMap[stringToEnum<Jobs>(profile.job)],
                onEditField = { selectedOption ->
                    val selectedJobOption = jobsMap.entries.find { it.value == selectedOption }?.key
                    if (selectedJobOption != null) {
                        profile.job = enumToString(selectedJobOption)
                    }
                },
                includeNullOption = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileDropdownField(
                label = stringResource(R.string.what_type_of_relationship),
                options = relationshipTypeMap.values.toList(),
                selectedOption = relationshipTypeMap[profile.relationshipTypeEnum]!!,
                onEditField = { selectedOption ->
                    val relationshipTypeOption =
                        relationshipTypeMap.entries.find { it.value == selectedOption }?.key
                    if (relationshipTypeOption != null) {
                        profile.relationshipType = relationshipTypeOption.toString()
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSection(
                title = stringResource(R.string.more_about_me),
                rowTitles = listOf(
                    "horoscope" to horoscopeMap[profile.horoscopeEnum],
                    "education" to educationMap[stringToEnum<Education>(profile.education)],
                    "personality_type" to personalityMap[stringToEnum<Personality>(profile.personalityType)]
                ),
                onSelectedItemChange = { field, selectedOption ->
                    when (field) {
                        "horoscope" -> {
                            val horoscopeOption =
                                horoscopeMap.entries.find { it.value == selectedOption }?.key
                            if (horoscopeOption != null) {
                                profile.horoscope = horoscopeOption.toString()
                            }
                        }

                        "education" -> {

                            val educationOption =
                                educationMap.entries.find { it.value == selectedOption }?.key
                            if (educationOption != null) {
                                profile.education = enumToString(educationOption)
                            }
                        }

                        "personality_type" -> {
                            var personalityOption =
                                personalityMap.entries.find { it.value == selectedOption }?.key
                            if (personalityOption != null) {
                                profile.personalityType = enumToString(personalityOption)
                            }
                        }

                        else -> println("Campo desconocido: $field")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSection(
                title = stringResource(R.string.lifestyle),
                rowTitles = listOf(
                    "pets" to petsMap[stringToEnum<Pets>(profile.pets)],
                    "drinks" to habitsMap[profile.drinksEnum],
                    "smokes" to habitsMap[profile.smokesEnum],
                    "sports" to habitsMap[profile.doesSportsEnum],
                    "religion" to religionMap[profile.valuesAndBeliefsEnum]
                ),
                onSelectedItemChange = { field, selectedOption ->
                    when (field) {
                        "pets" -> {
                            val petsOption =
                                petsMap.entries.find { it.value == selectedOption }?.key
                            if (petsOption != null) {
                                profile.pets = enumToString(petsOption)
                            }
                        }

                        "drinks" -> profile.drinks =
                            habitsMap.entries.find { it.value == selectedOption }?.key.toString()

                        "smokes" -> profile.smokes =
                            habitsMap.entries.find { it.value == selectedOption }?.key.toString()

                        "sports" -> profile.doesSports =
                            habitsMap.entries.find { it.value == selectedOption }?.key.toString()

                        "religion" -> profile.valuesAndBeliefs =
                            religionMap.entries.find { it.value == selectedOption }?.key.toString()

                        else -> println("Campo desconocido: $field")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LegalSection(onCookiesClick, onPrivacyClick)

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    profile.let {
                        Log.i("ProfileScreen", "Updating profile: $it")
                        profileViewModel.updateProfile(it)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.loading_error))
        }
    }
}
