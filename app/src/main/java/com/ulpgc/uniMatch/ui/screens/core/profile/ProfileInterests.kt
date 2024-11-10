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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.InterestGrid
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar


@Composable
fun ProfileInterests(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val interestsMapping = context.resources.getStringArray(R.array.interests).toList()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profile = profileViewModel.profileData.collectAsState().value
    val profileInterests = profile?.interests?.mapNotNull { interest ->
        val index = profile.interests.indexOf(interest)
        if (index >= 0 && index < interestsMapping.size) {
            interestsMapping[index]
        } else null
    } ?: emptyList()

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ProfileSettingsTopBar { navController.popBackStack() }

        Text(
            text = stringResource(R.string.interests),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        InterestGrid(profileInterests) { interest, isAdded ->
            if (isAdded) {
                profileViewModel.addInterest(interest)
            } else {
                profileViewModel.removeInterest(interest)
            }
        }
    }
}




