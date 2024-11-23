package com.ulpgc.uniMatch.ui.screens.core.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enums.Interests
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.InterestGrid
import com.ulpgc.uniMatch.ui.screens.utils.enumToString


@Composable
fun ProfileInterests(
    profileViewModel: ProfileViewModel,
) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }


    val profile = profileViewModel.profileData.collectAsState().value

    if (profile != null) {
        Log.i("ProfileInterests", "profile interests: ${profile.interests}")
        val interestsMap =
            context.resources.getStringArray(R.array.interests).mapIndexed { index, name ->
                Interests.entries.getOrNull(index) to name
            }.toMap()

        var profileInterests = interestsMap.mapNotNull { entry ->
            if (profile.interests.contains(enumToString(entry.key))) {
                entry.value
            } else {
                null
            }
        }

        val addedInterests = profile.interests.toMutableList()

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Text(
                text = stringResource(R.string.interests),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            InterestGrid(profileInterests) { interest, isAdded ->
                val key = interestsMap.entries.find { it.value == interest }?.key
                Log.i("ProfileInterests", "profileInterests: $profileInterests")
                if (key != null) {
                    if (isAdded) {
                        enumToString(key)?.let { addedInterests.add(it) }
                        profileViewModel.updateInterests(addedInterests)
                    } else {
                        enumToString(key)?.let { addedInterests.remove(it) }
                        profileViewModel.updateInterests(addedInterests)
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




