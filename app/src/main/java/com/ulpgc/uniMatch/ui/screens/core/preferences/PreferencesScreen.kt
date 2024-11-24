package com.ulpgc.uniMatch.ui.screens.core.preferences

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.infrastructure.viewModels.PermissionsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.DropdownMenu

@Composable
fun PreferencesScreen(
    profileViewModel: ProfileViewModel,
    permissionsViewModel: PermissionsViewModel
) {
    val profile by profileViewModel.profileData.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    var maxDistance by remember { mutableIntStateOf(0) }
    var genderPriority by remember { mutableStateOf<Gender?>(null) }
    var ageRange by remember { mutableStateOf(18 to 100) }
    var relationshipType by remember { mutableStateOf(RelationshipType.FRIENDSHIP) }

    val context = LocalContext.current

    val genderMap = context.resources.getStringArray(R.array.genders).mapIndexed { index, name ->
        Gender.entries[index] to name
    }.toMap()

    val relationshipTypeMap = context.resources.getStringArray(R.array.relationship_type).mapIndexed { index, name ->
        RelationshipType.entries[index] to name
    }.toMap()

    val minAge = 18f
    val maxAge = 100f

    var ageMin by remember { mutableStateOf(ageRange.first.toFloat()) }
    var ageMax by remember { mutableStateOf(ageRange.second.toFloat()) }


    LaunchedEffect(profile) {
        profile?.let {
            maxDistance = it.maxDistance
            genderPriority = it.genderPriority
            ageRange = it.ageRange.min to it.ageRange.max
            relationshipType = it.relationshipType
            ageMin = it.ageRange.min.toFloat()
            ageMax = it.ageRange.max.toFloat()
        }
    }


    Column(modifier = Modifier.padding(16.dp)) {

        if (profile == null) {
            Text(
                text = stringResource(id = R.string.no_preferences_info),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            Text(
                text = stringResource(id = R.string.max_distance, maxDistance),
                color = MaterialTheme.colorScheme.onBackground
            )
            Slider(
                value = maxDistance.toFloat(),
                onValueChange = { maxDistance = it.toInt() },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    if (profile?.location != null) {
                        Log.i("ProfileLocation", profile?.location.toString())
                        profileViewModel.updateMaxDistance(maxDistance)
                    } else {
                        maxDistance = 0
                    }
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.gender_priority_title),
                color = MaterialTheme.colorScheme.onBackground
            )
            var expandedGender by remember { mutableStateOf(false) }
            Box {
                genderPriority?.let {
                    Text(
                        text = it.name,
                        modifier = Modifier.clickable { expandedGender = true },
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    items = genderMap.values.toList(),
                    selectedItem = genderMap[genderPriority],
                    onItemSelected = { selectedGender ->
                        var genderOption = genderMap.entries.find { it.value == selectedGender }?.key
                        if (genderOption != null) {
                            genderPriority = genderOption
                        }

                        profileViewModel.updateGenderPriority(genderPriority)
                    },
                    includeNullOption = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    id = R.string.age_range_title,
                    ageRange.first,
                    ageRange.second
                ),
                color = MaterialTheme.colorScheme.onBackground
            )



            RangeSlider(
                value = ageMin..ageMax,
                onValueChange = { newValue ->
                    ageMin = newValue.start
                    ageMax = newValue.endInclusive
                    ageRange = ageMin.toInt() to ageMax.toInt()
                },
                valueRange = minAge..maxAge,
                onValueChangeFinished = {
                    profileViewModel.updateAgeRange(ageRange.first, ageRange.second)
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.relationship_type_title),
                color = MaterialTheme.colorScheme.onBackground
            )
            var expandedRelationship by remember { mutableStateOf(false) }
            Box {
                relationshipType.let {
                    Text(
                        text = it.name,
                        modifier = Modifier.clickable { expandedRelationship = true },
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    DropdownMenu(
                        items = relationshipTypeMap.values.toList(),
                        selectedItem = relationshipTypeMap[it],
                        onItemSelected = { selectedRelationship ->

                            var relationshipOption = relationshipTypeMap.entries.find { it.value == selectedRelationship }?.key
                            if (relationshipOption != null) {
                                relationshipType = relationshipOption
                                profileViewModel.updateRelationshipType(relationshipType)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.preferences_explanation),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
