package com.ulpgc.uniMatch.ui.screens.core.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel
) {
    val matchingProfiles by homeViewModel.matchingProfiles.collectAsState(emptyList())
    val userId = authViewModel.userId ?: return

    LaunchedEffect(Unit) {
        homeViewModel.loadMatchingUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (matchingProfiles.isEmpty()) {
            Text("Loading profiles...", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
        } else {
            CardStack(
                profiles = matchingProfiles,
                onSwipeLeft = { profile ->
                    homeViewModel.dislikeUser(userId, profile.userId)
                },
                onSwipeRight = { profile ->
                    homeViewModel.likeUser(userId, profile.userId)
                },
                onRequestMoreProfiles = {
                    homeViewModel.loadMoreMatchingUsers()
                }
            )
        }
    }
}

@Composable
fun CardStack(
    profiles: List<Profile>,
    onSwipeLeft: (Profile) -> Unit,
    onSwipeRight: (Profile) -> Unit,
    onRequestMoreProfiles: () -> Unit
) {
    var index by remember { mutableIntStateOf(0) }

    // Si quedan solo 3 o menos perfiles, solicitar más
    if (index >= profiles.size - 3) {
        Log.i("CardStack", "Requesting more profiles")
        onRequestMoreProfiles()
    }

    if (index < profiles.size) {
        ProfileCard(
            profile = profiles[index],
            onSwipeLeft = {
                onSwipeLeft(profiles[index])
                index++
            },
            onSwipeRight = {
                onSwipeRight(profiles[index])
                index++
            }
        )
    } else {
        Text("No more profiles", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun ProfileCard(
    profile: Profile,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    var currentImageIndex by remember(profile) { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo actual desde el array `profile.wall`
        AsyncImage(
            model = profile.wall[currentImageIndex],
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.9f),
                            Color.Black.copy(alpha = 1.0f),
                            Color.Black.copy(alpha = 1.0f),
                            Color.Black.copy(alpha = 1.0f)
                        ),

                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {

            Text(profile.name, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text("${profile.age} years old", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text(
                profile.aboutMe,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSwipeLeft) {
                    Icon(Icons.Default.Close, contentDescription = "Dislike", tint = Color.White)
                }

                IconButton(
                    onClick = {
                        currentImageIndex = (currentImageIndex + 1) % profile.wall.size
                    }
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Cambiar Imagen", tint = Color.White)
                }

                IconButton(onClick = onSwipeRight) {
                    Icon(Icons.Default.Favorite, contentDescription = "Like", tint = Color.White)
                }
            }
        }
    }
}

