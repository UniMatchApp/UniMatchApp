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
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.viewModels.PermissionsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.DropdownMenu
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper
import com.ulpgc.uniMatch.ui.theme.MainColor


@Composable
fun PreferencesScreen(
    profileViewModel: ProfileViewModel,
    permissionsViewModel: PermissionsViewModel
) {
    val profile by profileViewModel.profileData.collectAsState()

    val context = LocalContext.current

    val hasLocationPermission = permissionsViewModel.hasLocationPermissions.collectAsState().value

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    LaunchedEffect(hasLocationPermission) {
        val location = LocationHelper.getCurrentLocation(context)
        location?.let {
            var loc = Profile.Location(
                latitude = it.second,
                longitude = it.first,
                altitude = null
            )
            profileViewModel.updateLocation(loc)
        }
    }

    val genderMap = context.resources.getStringArray(R.array.genders).mapIndexed { index, name ->
        Gender.entries[index] to name
    }.toMap()

    val relationshipTypeMap = context.resources.getStringArray(R.array.relationship_type).mapIndexed { index, name ->
        RelationshipType.entries[index] to name
    }.toMap()


    Column(modifier = Modifier.padding(16.dp)) {

        if (profile == null) {
            Text(
                text = stringResource(id = R.string.no_preferences_info),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            var maxDistance by remember { mutableIntStateOf(profile?.maxDistance ?: 0) }
            var genderPriority by remember { mutableStateOf<Gender?>(profile!!.genderPriority) }

            var relationshipType by remember { mutableStateOf(profile!!.relationshipType) }

            var ageRange by remember { mutableStateOf(18 to 100) }

            val minAge = 18f
            val maxAge = 100f

            var ageMin by remember { mutableStateOf(profile?.ageRange?.min?.toFloat() ?: ageRange.first.toFloat()) }
            var ageMax by remember { mutableStateOf(profile?.ageRange?.max?.toFloat() ?: ageRange.second.toFloat()) }

            Text(
                text = stringResource(id = R.string.max_distance, maxDistance),
                color = MaterialTheme.colorScheme.onBackground
            )
            if (!hasLocationPermission) {
                Text(
                    text = stringResource(id = R.string.location_permission_required),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Slider(
                value = maxDistance.toFloat(),
                onValueChange = {
                    maxDistance = it.toInt()
                },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    if (profile?.location != null) {
                        profileViewModel.updateMaxDistance(maxDistance)
                    } else {
                        maxDistance = 0
                    }
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                enabled = hasLocationPermission
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
                        profileViewModel.updateGenderPriority(genderOption)
                    },
                    includeNullOption = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    id = R.string.age_range_title,
                    ageMin.toInt(),
                    ageMax.toInt()
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
                ),
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
                                profileViewModel.updateRelationshipType(relationshipOption)
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
