package com.ulpgc.uniMatch.ui.screens.core.profile

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
    onEditInterestsClick: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val context = LocalContext.current

    val profile = profileViewModel.profileData.collectAsState().value

    var aboutMeText by remember { mutableStateOf(profile?.aboutMe ?: "") }
    val facts = context.resources.getStringArray(R.array.funny_questions).toList()
    var selectedFact by remember { mutableStateOf(profile?.fact ?: "") }
    var selectedInterests by remember { mutableStateOf(profile?.interests ?: emptyList()) }
    var selectedHeight by remember { mutableStateOf(profile?.height?.toString() ?: "170") }
    var selectedWeight by remember { mutableStateOf(profile?.weight?.toString() ?: "70") }
    var selectedGender by remember { mutableStateOf(profile?.gender ?: "") }
    var selectedOrientation by remember { mutableStateOf(profile?.sexualOrientation ?: "") }
    var selectedJob by remember { mutableStateOf(profile?.job ?: "") }
    var selectedRelationshipType by remember { mutableStateOf(profile?.relationshipType ?: "") }
    var selectedHoroscope by remember { mutableStateOf(profile?.horoscope ?: "") }
    var selectedEducation by remember { mutableStateOf(profile?.education ?: "") }
    var selectedPersonalityType by remember { mutableStateOf(profile?.personalityType ?: "") }
    var selectedPets by remember { mutableStateOf(profile?.pets ?: "") }
    var selectedDrinks by remember { mutableStateOf(profile?.drinks ?: "") }
    var selectedSmokes by remember { mutableStateOf(profile?.smokes ?: "") }
    var selectedSports by remember { mutableStateOf(profile?.doesSports ?: "") }
    var selectedReligion by remember { mutableStateOf(profile?.valuesAndBeliefs ?: "") }

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
                    .data(profile?.preferredImage)
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
                IconButton(onClick = { profile?.let { onEditClick(it.profileId) } }) {
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
            text = "${profile?.name ?: "Nombre no disponible"}, ${profile?.age ?: "--"}",
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

        profile?.fact?.let {
            DropdownMenu(
                items = facts,
                selectedItem = it,
                onItemSelected = { newFact -> selectedFact = newFact }
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
                .clickable { profile?.let { onEditInterestsClick(it.profileId) } }
                .border(width = 1.dp, color = Color.Gray) // Bordes
                .padding(16.dp)
        ) {
            Text(
                text = profile?.interests?.joinToString(", ") ?: "Selecciona tus intereses",
                modifier = Modifier.fillMaxWidth()
            )
        }



        ProfileDropdownField(label = "Sexo", options = context.resources.getStringArray(R.array.genders).toList(), onEditField = { selectedGender = it })
        ProfileInputField(
            label = "Altura en cm",
            initialValue = profile?.height?.toString() ?: "170",
            onValueChange = { newHeight -> selectedHeight = newHeight }
        )

        ProfileInputField(
            label = "Peso en kg",
            initialValue = profile?.weight?.toString() ?: "70",
            onValueChange = { newWeight -> selectedWeight = newWeight }
        )
        ProfileDropdownField(label = "Orientación sexual", options = context.resources.getStringArray(R.array.sexual_orientation).toList(), onEditField = { selectedOrientation = it })
        ProfileDropdownField(label = "Puesto", options = listOf("Ingeniero", "Médico", "Profesor", "Diseñador"), onEditField = { selectedJob = it })
        ProfileDropdownField(label = "¿Qué tipo de relación buscas?", options = context.resources.getStringArray(R.array.relationship_type).toList(), onEditField = { selectedRelationshipType = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Section: Más sobre mí
        ProfileSection(
            title = "Más sobre mí",
            rowTitles = listOf(
                "horoscope" to profile?.horoscope.toString(),
                "education" to profile?.education,
                "personality_type" to profile?.personalityType
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Section: Estilo de vida
        ProfileSection(
            title = "Estilo de vida",
            rowTitles = listOf(
                "pets" to profile?.pets,
                "drinks" to profile?.drinks,
                "smokes" to profile?.smokes,
                "sports" to profile?.doesSports,
                "religion" to profile?.valuesAndBeliefs
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LegalSection()

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (aboutMeText != profile?.aboutMe) profileViewModel.updateAboutMe(aboutMeText)
                if (selectedFact != profile?.fact) profileViewModel.updateFact(selectedFact)
                if (selectedInterests != profile?.interests) profileViewModel.updateInterests(selectedInterests)
//                if (selectedHeight != profile?.height?.toString()) profileViewModel.updateHeight(selectedHeight.toInt())
//                if (selectedWeight != profile?.weight?.toString()) profileViewModel.updateWeight(selectedWeight.toInt())
//                if (selectedJob != profile?.job) profileViewModel.updateJob(selectedJob)
//                if (selectedEducation != profile?.education) profileViewModel.updateEducation(selectedEducation)
//                if (selectedPersonalityType != profile?.personalityType) profileViewModel.updatePersonalityType(selectedPersonalityType)
//                if (selectedPets != profile?.pets) profileViewModel.updatePets(selectedPets)
//                if (selectedDrinks != profile?.drinks) profileViewModel.updateDrinks(selectedDrinks)
//                if (selectedSmokes != profile?.smokes) profileViewModel.updateSmokes(selectedSmokes)
//                if (selectedSports != profile?.doesSports) profileViewModel.updateDoesSports(selectedSports)
//                if (selectedReligion != profile?.valuesAndBeliefs) profileViewModel.updateValuesAndBeliefs(selectedReligion)
//                if (selectedOrientation != profile?.sexualOrientation) profileViewModel.updateSexualOrientation(selectedOrientation)
//                if (selectedHoroscope != profile?.horoscope) profileViewModel.updateHoroscope(selectedHoroscope)
//                if (selectedRelationshipType != profile?.relationshipType) profileViewModel.updateRelationshipType(selectedRelationshipType)
//                if (selectedGender != profile?.gender) profileViewModel.updateGender(selectedGender)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }




    }
}
