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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.DropdownMenu
import com.ulpgc.uniMatch.ui.components.profile.LegalSection
import com.ulpgc.uniMatch.ui.components.profile.ProfileDropdownField
import com.ulpgc.uniMatch.ui.components.profile.ProfileInputField
import com.ulpgc.uniMatch.ui.components.profile.ProfileSection

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onEditClick: (String) -> Unit,
    onEditInterestsClick: (String) -> Unit,
    onCookiesClick : () -> Unit,
    onPrivacyClick : () -> Unit,
) {

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val context = LocalContext.current

    var profile = profileViewModel.profileData.collectAsState().value

    val isLoading by profileViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if( profile != null) {
        val facts = context.resources.getStringArray(R.array.funny_questions).toList()

        var aboutMeText by remember { mutableStateOf(profile.aboutMe ?: "") }

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
                    .size(150.dp)
                    .background(Color.Gray, CircleShape),
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
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                        .align(Alignment.TopEnd)
                        .clickable {  }
                        .padding(4.dp)
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


            Text(
                text = "${profile.name ?: "Nombre no disponible"}, ${profile.age ?: "--"}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sobre mí",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray,
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
                text = "Preguntas",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )

            profile.fact?.let {
                DropdownMenu(
                    items = facts,
                    selectedItem = it,
                    onItemSelected = { newFact -> profile.fact = newFact }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Intereses",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray,
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
                    text = profile.interests?.joinToString(", ") ?: "Selecciona tus intereses",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ProfileDropdownField(
                label = "Sexo",
                options = context.resources.getStringArray(R.array.genders).toList(),
                selectedOption = profile.gender.toString() ?: "Seleccionar",
                onEditField = { selectedOption ->
                    val option = Gender.values().firstOrNull { it.name == selectedOption.toUpperCase() }

                    if (option != null) {
                        profile.gender = option
                    } else {
                        // Manejo de error en caso de que no coincida con ningún valor de Gender
                        println("El valor '$selectedOption' no corresponde a ningún género válido.")
                    }
                }
            )

            ProfileInputField(
                label = "Altura en cm",
                initialValue = profile.height.toString() ?: "170",
                onValueChange = { newHeight -> profile.height = newHeight.toInt() }
            )

            ProfileInputField(
                label = "Peso en kg",
                initialValue = profile.weight?.toString() ?: "70",
                onValueChange = { newWeight -> profile.weight = newWeight.toInt() }
            )
            ProfileDropdownField(
                label = "Orientación sexual",
                options = context.resources.getStringArray(R.array.sexual_orientation).toList(),
                selectedOption = profile.sexualOrientation?.name ?: "Seleccionar",
                onEditField = { selectedOption ->
                    val option = SexualOrientation.values().firstOrNull { it.name == selectedOption.toUpperCase() }

                    if (option != null) {
                        profile.sexualOrientation = option
                    } else {
                        println("El valor '$selectedOption' no corresponde a ninguna orientación sexual válida.")
                    }
                }
            )

            ProfileDropdownField(
                label = "Puesto",
                options = context.resources.getStringArray(R.array.jobs).toList(),
                selectedOption = profile.job ?: "Seleccionar",
                onEditField = { profile.job = it }
            )

            ProfileDropdownField(
                label = "¿Qué tipo de relación buscas?",
                options = context.resources.getStringArray(R.array.relationship_type).toList(),
                selectedOption = profile.relationshipType?.toString() ?: "Seleccionar",
                onEditField = { selectedOption ->
                    val option = RelationshipType.values().firstOrNull { it.name == selectedOption.toUpperCase() }

                    if (option != null) {
                        profile.relationshipType = option
                    } else {
                        println("El valor '$selectedOption' no corresponde a ningún tipo de relación válido.")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSection(
                title = "Más sobre mí",
                rowTitles = listOf(
                    "horoscope" to profile.horoscope.toString().lowercase().replaceFirstChar { it.uppercase() },
                    "education" to profile.education,
                    "personality_type" to profile.personalityType
                ),
                onSelectedItemChange = { field, selectedOption ->
                    when (field) {
                        "horoscope" -> {
                            val horoscopeOption = Horoscope.values().firstOrNull { it.name == selectedOption.toUpperCase() }
                            if (horoscopeOption != null) {
                                profile.horoscope = horoscopeOption
                            } else {
                                println("El valor '$selectedOption' no corresponde a un horóscopo válido.")
                            }
                        }
                        "education" -> profile.education = selectedOption
                        "personality_type" -> profile.personalityType = selectedOption
                        else -> println("Campo desconocido: $field")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSection(
                title = "Estilo de vida",
                rowTitles = listOf(
                    "pets" to profile.pets,
                    "drinks" to profile.drinks,
                    "smokes" to profile.smokes,
                    "sports" to profile.doesSports,
                    "religion" to profile.valuesAndBeliefs
                ),
                onSelectedItemChange = { field, selectedOption ->
                    when (field) {
                        "pets" -> profile.pets = selectedOption
                        "drinks" -> profile.drinks = selectedOption
                        "smokes" -> profile.smokes = selectedOption
                        "sports" -> profile.doesSports = selectedOption
                        "religion" -> profile.valuesAndBeliefs = selectedOption
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
                        profileViewModel.updateProfile(it)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No se ha podido cargar el perfil")
        }
    }


}
