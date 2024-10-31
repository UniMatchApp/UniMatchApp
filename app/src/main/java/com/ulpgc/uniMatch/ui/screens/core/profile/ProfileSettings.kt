package com.ulpgc.uniMatch.ui.screens.core.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.LegalSection
import com.ulpgc.uniMatch.ui.components.profile.ProfileDropdownField
import com.ulpgc.uniMatch.ui.components.profile.ProfileInputField
import com.ulpgc.uniMatch.ui.components.profile.ProfileSection
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar

@Composable
fun ProfileSettings(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profile = profileViewModel.profileData.collectAsState().value
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileSettingsTopBar(onBackPressed = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ProfileDropdownField(label = "Sexo", options = context.resources.getStringArray(R.array.genders).toList())
            ProfileInputField(
                label = "Altura en cm",
                initialValue = profile?.height?.toString() ?: "170cm",
                onValueChange = { newAltura -> /* Actualizar valor en el ViewModel */ }
            )

            ProfileInputField(
                label = "Peso en kg",
                initialValue = profile?.weight?.toString() ?: "70kg",
                onValueChange = { newPeso -> /* Actualizar valor en el ViewModel */ }
            )
            ProfileDropdownField(label = "Orientación sexual", options = context.resources.getStringArray(R.array.sexual_orientation).toList())
            ProfileDropdownField(label = "Puesto", options = listOf("Ingeniero", "Médico", "Profesor", "Diseñador"))
            ProfileDropdownField(label = "¿Qué tipo de relación buscas?", options = context.resources.getStringArray(R.array.relationship_type).toList())

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
        }
    }
}












