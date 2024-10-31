package com.ulpgc.uniMatch.ui.screens.core.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.InputField
import com.ulpgc.uniMatch.ui.components.profile.ProfileSection
import com.ulpgc.uniMatch.ui.theme.Bone

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel
) {
    val matchingProfiles by homeViewModel.matchingProfiles.collectAsState(emptyList())
    val userId = authViewModel.userId ?: return
    var isModalOpen by remember { mutableStateOf(false) }
    var selectedProfile: Profile? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        homeViewModel.loadMatchingUsers()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (matchingProfiles.isEmpty()) {
                Text(
                    "Loading profiles...",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
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
                    },
                    onOpenProfile = { profile ->
                        selectedProfile = profile
                        isModalOpen = true
                    }
                )
            }
        }

        // Mostrar modal si `isModalOpen` es true, con un `zIndex` mayor
        if (isModalOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .zIndex(1f)
            ) {
                ProfileInfoModal(profile = selectedProfile) {
                    isModalOpen = false
                }
            }
        }
    }
}

@Composable
fun CardStack(
    profiles: List<Profile>,
    onSwipeLeft: (Profile) -> Unit,
    onSwipeRight: (Profile) -> Unit,
    onRequestMoreProfiles: () -> Unit,
    onOpenProfile: (Profile) -> Unit
) {
    var isRequestingMore by remember { mutableStateOf(false) }

    if (profiles.size < 3 && !isRequestingMore) {
        isRequestingMore = true
        onRequestMoreProfiles()
    }

    LaunchedEffect(profiles.size) {
        isRequestingMore = false
        Log.i("CardStack", "Profiles: ${profiles.size}")
    }

    if (profiles.isNotEmpty()) {
        ProfileCard(
            profile = profiles[0],
            onSwipeLeft = {
                onSwipeLeft(profiles[0])
                Log.i("CardStack", "Swipe left")
            },
            onSwipeRight = {
                onSwipeRight(profiles[0])
            },
            onOpenProfile = {
                onOpenProfile(profiles[0])
            }
        )
    } else {
        Text("No more profiles", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard(
    profile: Profile,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onOpenProfile: () -> Unit
) {
    var currentImageIndex by remember(profile) { mutableIntStateOf(0) }
    val swipeThreshold = 200f
    var accumulatedDrag by remember { mutableStateOf(0f) }
    var isTracking by remember { mutableStateOf(false) }
    var showLikeDislike by remember { mutableStateOf(false) }
    var isLike by remember { mutableStateOf(true) }

    val iconDisplayDuration = 500L

    // Animaciones para rotación y desplazamiento
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isTracking) accumulatedDrag else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    val rotation by animateFloatAsState(
        targetValue = if (showLikeDislike) 20f else 0f, // Rotación cuando se hace swipe
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        if (!isTracking) {
                            isTracking = true
                            accumulatedDrag = 0f
                        }
                    },
                    onDragEnd = {
                        isTracking = false
                        accumulatedDrag = 0f
//                        showLikeDislike = false
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        accumulatedDrag += dragAmount
                        if (kotlin.math.abs(accumulatedDrag) > swipeThreshold) {
                            isLike = accumulatedDrag < 0
                            showLikeDislike = true
                            if (isLike) onSwipeRight() else onSwipeLeft()
                            isTracking = false
                            accumulatedDrag = 0f
                        }
                    }
                )
            }
            .clickable {
                currentImageIndex = (currentImageIndex + 1) % profile.wall.size
            }
            .offset { IntOffset(animatedOffsetX.toInt(), 0) }
    ) {
        AsyncImage(
            model = profile.wall[currentImageIndex],
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Icono de Like/Dislike con animación y temporizador
        AnimatedVisibility(
            visible = showLikeDislike,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = if (isLike) Icons.Filled.Favorite else Icons.Filled.Close,
                contentDescription = if (isLike) "Like" else "Dislike",
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer(
                        rotationZ = rotation,
                        alpha = 0.9f
                    ),
                tint = if (isLike) Color.Red else Color.Gray
            )

            // Controla la visibilidad del icono después de un retraso
            LaunchedEffect(showLikeDislike) {
                if (showLikeDislike) {
                    kotlinx.coroutines.delay(iconDisplayDuration)
                    showLikeDislike = false
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            profile.wall.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            color = if (index == currentImageIndex) Color.White else Color.Gray.copy(
                                alpha = 0.5f
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.9f),
                            Color.Black.copy(alpha = 1.0f)
                        )
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = ", ${profile.age}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Light),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                            .clickable {
                                onOpenProfile()
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Intereses",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Intereses",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Intereses",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
                ) {
                    profile.interests.forEachIndexed { index, interest ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = interest,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }

                        if (index < profile.interests.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onSwipeLeft) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Dislike",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            currentImageIndex = (currentImageIndex + 1) % profile.wall.size
                        }
                    ) {
                        Icon(
                            Icons.Default.SwapHoriz,
                            contentDescription = "Cambiar Imagen",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = onSwipeRight) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Like",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileInfoModal(profile: Profile?, onClose: () -> Unit) {
    if (profile != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { onClose() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Cerrar",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscando",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Busco...",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputField(
                            value = profile.relationshipType.toString(),
                            onValueChange = {},
                            label = "Relación",
                            textColor = Color.Black,
                            isEditable = false,
                            backgroundColor = Bone,
                            isRound = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    ProfileSection(
                        title = "Más sobre mí",
                        rowTitles = listOf(
                            "horoscope" to profile.horoscope.toString(),
                            "education" to profile.education,
                            "personality_type" to profile.personalityType
                        ),
                        isSelectable = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    ProfileSection(
                        title = "Estilo de vida",
                        rowTitles = listOf(
                            "pets" to profile.pets,
                            "drinks" to profile.drinks,
                            "smokes" to profile.smokes,
                            "sports" to profile.doesSports,
                            "religion" to profile.valuesAndBeliefs
                        ),
                        isSelectable = true
                    )
                }
            }
        }
    }
}

