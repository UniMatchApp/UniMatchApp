package com.ulpgc.uniMatch.ui.screens.core.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.DropdownMenu

@Composable
fun PreferencesScreen(
    profileViewModel: ProfileViewModel
) {
    val profile by profileViewModel.profileData.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    // Variables para preferencias
    var maxDistance by remember { mutableStateOf(0) }
    var genderPriority by remember { mutableStateOf(Gender.MALE) }
    var ageRange by remember { mutableStateOf(18 to 100) } // o cualquier valor predeterminado
    var relationshipType by remember { mutableStateOf(RelationshipType.FRIENDSHIP) }

    LaunchedEffect(profile) {
        profile?.let {
            maxDistance = it.maxDistance
            genderPriority = it.genderPriority!!
            ageRange = it.ageRange
            relationshipType = it.relationshipType
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Preferences", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar mensaje si no hay perfil
        if (profile == null) {
            Text(
                text = "No hay información de preferencias disponible.",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            // Max Distance Slider
            Text(text = "Maximum Distance: $maxDistance km")
            Slider(
                value = maxDistance.toFloat(),
                onValueChange = { maxDistance = it.toInt() },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    profileViewModel.updateMaxDistance(maxDistance)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Priority Selector
            Text(text = "Gender Priority")
            var expandedGender by remember { mutableStateOf(false) }
            Box {
                Text(
                    text = genderPriority.name,
                    modifier = Modifier.clickable { expandedGender = true }
                )

                DropdownMenu(
                    items = Gender.entries.map { it.name },
                    selectedItem = genderPriority.name,
                    onItemSelected = { selectedGender ->
                        genderPriority = Gender.valueOf(selectedGender)
                        profileViewModel.updateGenderPriority(genderPriority)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Age Range: ${ageRange.first.toInt()} - ${ageRange.second.toInt()}")

            val minAge = 18f
            val maxAge = 100f

            val ageMin = ageRange.first.coerceIn(minAge.toInt(), maxAge.toInt()).toFloat()
            val ageMax = ageRange.second.coerceIn(minAge.toInt(), maxAge.toInt()).toFloat()



            RangeSlider(
                value = ageMin..ageMax,
                onValueChange = { newValue ->
                    val min = newValue.start.toInt()
                    val max = newValue.endInclusive.toInt()
                    ageRange = min to max
                },
                valueRange = minAge..maxAge,
                onValueChangeFinished = {

                    profileViewModel.updateAgeRange(ageRange.first, ageRange.second)
                },
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Relationship Type Selector
            Text(text = "Relationship Type")
            var expandedRelationship by remember { mutableStateOf(false) }
            Box {
                relationshipType.let {
                    Text(
                        text = it.name,
                        modifier = Modifier.clickable { expandedRelationship = true }
                    )

                    DropdownMenu(
                        items = RelationshipType.entries.map { it.name },
                        selectedItem = it.name,
                        onItemSelected = { selectedRelationship ->
                            relationshipType = RelationshipType.valueOf(selectedRelationship)
                            profileViewModel.updateRelationshipType(relationshipType)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Unimatch usa estas preferencias para sugerir matches. Algunas sugerencias pueden no estar dentro de tus parámetros.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

