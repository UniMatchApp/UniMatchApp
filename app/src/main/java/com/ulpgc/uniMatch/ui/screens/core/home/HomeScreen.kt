package com.ulpgc.uniMatch.ui.screens.core.home

import ProfileInfoModal
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.home.ConfirmBlockDialog
import com.ulpgc.uniMatch.ui.components.home.ProfileCard
import com.ulpgc.uniMatch.ui.theme.MainColor


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    userViewModel: UserViewModel,
    errorViewModel: ErrorViewModel
) {
    val matchingProfiles by homeViewModel.matchingProfiles.collectAsState()
    val userId = userViewModel.userId ?: return
    var isModalOpen by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }
    var selectedProfile: Profile? by remember { mutableStateOf(null) }
    val maxRetries = 1
    val isLoading by homeViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadMatchingUsers()
    }

    LaunchedEffect(matchingProfiles.size) {
        if (matchingProfiles.size < 5 && matchingProfiles.isNotEmpty()) {
            homeViewModel.loadMoreMatchingUsers()
        }
    }

    if (isLoading && matchingProfiles.isEmpty()) {
        LoadingIndicator()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (matchingProfiles.isNotEmpty()) {
                val profileToDisplay = matchingProfiles.first()
                profileToDisplay.let { profile ->
                    key(profile.userId) {
                        ProfileCard(
                            profile = profile,
                            onSwipeLeft = {
                                homeViewModel.dislikeUser(profile.userId)
                            },
                            onSwipeRight = {
                                homeViewModel.likeUser(profile.userId)
                            },
                            onOpenProfile = { selectedProfile = profile; isModalOpen = true }
                        )
                    }
                }
            } else if (!isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "No matches found",
                        modifier = Modifier.size(96.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.no_founded_profiles),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Button(
                        onClick = { homeViewModel.loadMatchingUsers() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.try_again), color = Color.White)
                    }
                }
            }
        }

        if (isModalOpen) {
            ProfileInfoModal(
                profile = selectedProfile,
                onClose = { isModalOpen = false },
                onReport = { reason, detail, extraDetails ->
                    isModalOpen = false
                    homeViewModel.reportUser(
                        selectedProfile?.userId ?: "",
                        reason,
                        detail,
                        extraDetails
                    )
                },
                onBlock = {
                    isModalOpen = false
                    showBlockDialog = true
                }
            )
        }

        if (showBlockDialog) {
            ConfirmBlockDialog(
                onConfirm = {
                    homeViewModel.blockUser(selectedProfile?.userId ?: "")
                    showBlockDialog = false
                },
                onDismiss = { showBlockDialog = false }
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}


