package com.ulpgc.uniMatch.ui.screens.core.home

import ProfileInfoModal
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.ui.components.home.ConfirmBlockDialog
import com.ulpgc.uniMatch.ui.components.home.ProfileCard


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel
) {
    val matchingProfiles by homeViewModel.matchingProfiles.collectAsState()
    val userId = authViewModel.userId ?: return
    var isModalOpen by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }
    var selectedProfile: Profile? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        homeViewModel.loadMatchingUsers()
    }

    if (matchingProfiles.size < 5) {
        Log.i("HomeScreen", "Loading more profiles...")
        homeViewModel.loadMoreMatchingUsers()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (matchingProfiles.isEmpty()) {
                LoadingIndicator()
            } else {
                val profileToDisplay = matchingProfiles.first()

                ProfileCard(
                    profile = profileToDisplay,
                    onSwipeLeft = {
                        Log.i("HomeScreen", "Dislike profile: $profileToDisplay")
                        homeViewModel.dislikeUser(userId, profileToDisplay.userId)
                    },
                    onSwipeRight = {
                        Log.i("HomeScreen", "Like profile: $profileToDisplay")
                        homeViewModel.likeUser(userId, profileToDisplay.userId)
                    },
                    onOpenProfile = { selectedProfile = profileToDisplay; isModalOpen = true }
                )
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
    Text(
        stringResource(id = R.string.loading_profiles),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxSize()
    )
}


