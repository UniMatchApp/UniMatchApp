package com.ulpgc.uniMatch.ui.screens.core.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.InterestGrid
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar


@Composable
fun ProfileInterests(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profile = profileViewModel.profileData.collectAsState().value

    Column(modifier = Modifier.fillMaxWidth(),
        ) {

            ProfileSettingsTopBar { navController.popBackStack() }

            Text(
                text = "Intereses",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            InterestGrid(profile?.interests ?: emptyList()) { interest, isAdded ->
                if (isAdded) {
                    profileViewModel.addInterest(interest) // Llama a addInterest si se añadió
                } else {
                    profileViewModel.removeInterest(interest) // Llama a removeInterest si se eliminó
                }
            }
        }



}


