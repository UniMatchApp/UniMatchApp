package com.ulpgc.uniMatch.ui.components.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.enums.Interests
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.ui.screens.utils.enumToString

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard(
    profile: Profile,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onOpenProfile: () -> Unit
) {
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val swipeThreshold = 500f
    var accumulatedDrag by remember { mutableStateOf(0f) }
    var isTracking by remember { mutableStateOf(false) }
    var showLikeDislike by remember { mutableStateOf(false) }
    var isLike by remember { mutableStateOf(true) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isTracking) accumulatedDrag else 0f,
        animationSpec = tween(durationMillis = 400)
    )

    val rotationState by animateFloatAsState(
        targetValue = if (showLikeDislike) 360f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    // Determinar los colores de fondo según la dirección del deslizamiento
    val leftColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val rightColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    val backgroundColor = when {
        !isTracking -> MaterialTheme.colorScheme.background // Fondo del tema
        accumulatedDrag < 0 -> leftColor // Fondo rojo si se desliza a la izquierda
        else -> rightColor // Fondo rosado si se desliza a la derecha
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val clickAreaWidth = screenWidth / 2

    val context = LocalContext.current

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                        showLikeDislike = false
                        if (kotlin.math.abs(accumulatedDrag) < swipeThreshold) {
                            accumulatedDrag = 0f
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        accumulatedDrag += dragAmount
                        showLikeDislike = true
                        isLike = accumulatedDrag < 0

                        if (kotlin.math.abs(accumulatedDrag) > swipeThreshold) {
                            currentImageIndex = 0
                            Log.i("ProfileCard", "Swipe anywhere from drag")
                            if (isLike) onSwipeRight() else onSwipeLeft()
                            isTracking = false
                            accumulatedDrag = 0f

                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    val x = offset.x.toDp()

                    when {
                        x < clickAreaWidth && currentImageIndex > 0 -> {
                            currentImageIndex--
                        }

                        x > clickAreaWidth && currentImageIndex < profile.wall.size - 1 -> {
                            currentImageIndex++
                        }
                    }
                })
            }
            .offset { IntOffset(animatedOffsetX.toInt(), 0) }
            .animateContentSize()
    ) {
        AsyncImage(
            model = profile.wall[currentImageIndex],
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

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
                    .graphicsLayer(rotationZ = rotationState, alpha = 0.9f),
                tint = if (isLike) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )

            LaunchedEffect(showLikeDislike) {
                if (showLikeDislike) {
                    kotlinx.coroutines.delay(1500L)
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
                            color = if (index == currentImageIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(
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
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 1.0f)
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = ", ${profile.age}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Light),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                            .clickable { onOpenProfile() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = stringResource(R.string.interests),
                            tint = MaterialTheme.colorScheme.onBackground,
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
                        contentDescription = stringResource(R.string.interests),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.interests),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
                ) {
                    profileInterests.forEachIndexed { index, interest ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = interest,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
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
                    IconButton(onClick = {
                        currentImageIndex = 0;
                        Log.i("ProfileCard", "Swipe left from button")
                        onSwipeLeft()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

                    IconButton(
                        onClick = {
                            currentImageIndex = (currentImageIndex + 1) % profile.wall.size
                        }
                    ) {
                        Icon(
                            Icons.Default.SwapHoriz,
                            contentDescription = stringResource(R.string.next),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(onClick = {
                        currentImageIndex = 0;
                        Log.i("ProfileCard", "Swipe right from button")
                        onSwipeRight()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.like),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
