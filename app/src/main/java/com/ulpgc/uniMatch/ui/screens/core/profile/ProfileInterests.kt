package com.ulpgc.uniMatch.ui.screens.core.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enum.Interests
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.InterestGrid
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar
import com.ulpgc.uniMatch.ui.screens.utils.enumToString
import com.ulpgc.uniMatch.ui.screens.utils.stringToEnum


@Composable
fun ProfileInterests(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }


    val profile = profileViewModel.profileData.collectAsState().value

    val isLoading by profileViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if( profile != null) {
        Log.i("ProfileInterests", "profile interests: ${profile.interests}")
        val interestsMap = context.resources.getStringArray(R.array.interests).mapIndexed { index, name ->
            Interests.entries.getOrNull(index) to name
        }.toMap()

        var profileInterests = interestsMap.mapNotNull { entry ->
            if (profile.interests.contains(enumToString(entry.key))) {
                entry.value
            } else {
                null
            }
        }

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
                val key = interestsMap.entries.find { it.value == interest }?.key
                Log.i("ProfileInterests", "key: $key")
                if (key != null) {
                    if (isAdded) {
                        enumToString(key)?.let { profileViewModel.addInterest(it) }
                    } else {
                        enumToString(key)?.let { profileViewModel.removeInterest(it) }
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.loading_error))
        }
    }


}




