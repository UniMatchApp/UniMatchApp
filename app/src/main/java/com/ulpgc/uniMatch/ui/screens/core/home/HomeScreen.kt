package com.ulpgc.uniMatch.ui.screens.core.home

import ProfileInfoModal
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.ui.components.home.ConfirmBlockDialog
import com.ulpgc.uniMatch.ui.components.home.ProfileCard


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
    var retryCount by remember { mutableIntStateOf(0) }
    val maxRetries = 1

    val isLoading by homeViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadMatchingUsers()
    }

    if (matchingProfiles.isEmpty() && !isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No profiles found",
                textAlign = TextAlign.Center
            )
        }
    }

    if (isLoading) {
        LoadingIndicator()
    }

    LaunchedEffect(matchingProfiles.size) {
        if (matchingProfiles.size < 5 && retryCount < maxRetries) {
            retryCount++
            homeViewModel.loadMoreMatchingUsers()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (matchingProfiles.isNotEmpty()) {
                val profileToDisplay = matchingProfiles.first()
                Log.i("HomeScreen", "Displaying profile: $profileToDisplay")
                profileToDisplay.let { profile ->
                    key(profile.userId) {
                        ProfileCard(
                            profile = profile,
                            onSwipeLeft = {
                                Log.i("HomeScreen", "Dislike profile: $profile")
                                homeViewModel.dislikeUser(profile.userId)
                            },
                            onSwipeRight = {
                                Log.i("HomeScreen", "Like profile: $profile")
                                homeViewModel.likeUser(profile.userId)
                            },
                            onOpenProfile = { selectedProfile = profile; isModalOpen = true }
                        )
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
        CircularProgressIndicator()
    }
}


