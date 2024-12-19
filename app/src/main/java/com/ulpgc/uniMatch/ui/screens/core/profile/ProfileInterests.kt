package com.ulpgc.uniMatch.ui.screens.core.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enums.Interests
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.InterestGrid
import com.ulpgc.uniMatch.ui.screens.utils.enumToStringReplace

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
        val interestsMap =
            context.resources.getStringArray(R.array.interests).mapIndexed { index, name ->
                Interests.entries.getOrNull(index) to name
            }.toMap()

        // Usar un estado mutable para la lista de intereses seleccionados
        val addedInterests = remember { mutableStateOf(profile.interests.toMutableList()) }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Text(
                text = stringResource(R.string.interests),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            val profileInterests = interestsMap.mapNotNull { entry ->
                if (addedInterests.value.contains(enumToStringReplace(entry.key))) {
                    entry.value
                } else {
                    null
                }
            }

            InterestGrid(profileInterests) { interest, isAdded ->
                val key = interestsMap.entries.find { it.value == interest }?.key
                if (key != null) {
                    if (isAdded) {
                        enumToStringReplace(key)?.let {
                            addedInterests.value = (addedInterests.value + it).toMutableList() // Conversión a MutableList
                        }
                    } else {
                        enumToStringReplace(key)?.let {
                            addedInterests.value = (addedInterests.value - it).toMutableList() // Conversión a MutableList
                        }
                    }
                }
            }

            // Usar DisposableEffect para actualizar los intereses al salir de la pantalla
            DisposableEffect(Unit) {
                onDispose {
                    Log.i("ProfileInterests", "Updating interests: ${addedInterests.value}")
                    profileViewModel.updateInterests(addedInterests.value)
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.loading_error))
        }
    }
}
