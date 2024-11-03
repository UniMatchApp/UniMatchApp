package com.ulpgc.uniMatch.ui.screens.core.home

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.InputField
import com.ulpgc.uniMatch.ui.components.profile.ProfileSection
import com.ulpgc.uniMatch.ui.theme.Bone
import com.ulpgc.uniMatch.ui.theme.MainColor


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
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
                        homeViewModel.dislikeUser(
                            userId,
                            profileToDisplay.userId
                        ) // Lógica de rechazo
                    },
                    onSwipeRight = {
                        Log.i("HomeScreen", "Like profile: $profileToDisplay")
                        homeViewModel.likeUser(
                            userId,
                            profileToDisplay.userId
                        ) // Lógica de aceptación
                    },
                    onOpenProfile = { selectedProfile = profileToDisplay; isModalOpen = true }
                )
            }
        }

        // Mostrar el modal si hay un perfil seleccionado
        if (isModalOpen) {
            ProfileInfoModal(
                profile = selectedProfile,
                onClose = { isModalOpen = false },
                onReport = { reason, detail, extraDetails ->
                    isModalOpen = false
                    homeViewModel.reportUser(selectedProfile?.userId ?: "", reason, detail, extraDetails)
                },
                onBlock = {
                    isModalOpen = false
                    showBlockDialog = true
                }
            )
        }

        // Diálogo para confirmar el bloqueo
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
        "Loading profiles...",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ConfirmBlockDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bloquear usuario") },
        text = { Text("¿Estás seguro de que deseas bloquear a este usuario?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Bloquear")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


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

    LaunchedEffect(
        Log.i("ProfileCard", "Profile: $profile")
    ) { }

    // Estado para la rotación de los iconos
    val rotationState by animateFloatAsState(
        targetValue = if (showLikeDislike) 360f else 0f,
        animationSpec = tween(durationMillis = 1000) // Tiempo de rotación
    )

    // Determinar los colores de fondo según la dirección del deslizamiento
    val leftColor = Color.Red.copy(alpha = 0.5f)
    val rightColor = Color.Gray.copy(alpha = 0.5f)
    val backgroundColor = when {
        !isTracking -> Color.White // Fondo blanco si no se está haciendo swipe
        accumulatedDrag < 0 -> leftColor // Fondo rojo si se desliza a la izquierda
        else -> rightColor // Fondo verde si se desliza a la derecha
    }

    // Obtenemos el ancho de la pantalla en dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // La mitad del ancho de la pantalla
    val clickAreaWidth = screenWidth / 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Agrega el color de fondo aquí
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
                        x < clickAreaWidth -> {
                            if (currentImageIndex > 0) {
                                currentImageIndex--
                            }
                        }

                        x > clickAreaWidth -> {
                            if (currentImageIndex < profile.wall.size - 1) {
                                currentImageIndex++
                            }
                        }
                    }
                })
            }
            .offset { IntOffset(animatedOffsetX.toInt(), 0) }
            .animateContentSize()
    ) {
        // Imagen que cambia al hacer clic
        AsyncImage(
            model = profile.wall[currentImageIndex],
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
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
                    .graphicsLayer(rotationZ = rotationState, alpha = 0.9f), // Aplicar rotación
                tint = if (isLike) Color.Red else Color.Gray
            )

            LaunchedEffect(showLikeDislike) {
                if (showLikeDislike) {
                    kotlinx.coroutines.delay(1500L) // Mantener visible por más tiempo
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
                    IconButton(onClick = { onSwipeLeft() }) {
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

                    IconButton(onClick = { onSwipeRight() }) {
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
fun ProfileInfoModal(
    profile: Profile?,
    onClose: () -> Unit,
    onReport: (String, String, String) -> Unit,
    onBlock: () -> Unit
) {
    var showReportModal by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del modal de denuncia
    var selectedReason by remember { mutableStateOf("") }
    var selectedDetail by remember { mutableStateOf("") }
    var extraDetails by remember { mutableStateOf("") }

    if (profile != null && !showReportModal) { // Mostrar ProfileInfoModal solo si showReportModal es falso
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
                // Cerrar el modal
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

                // Sobre mi perfil
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sobre mí",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputField(
                            value = profile.aboutMe,
                            onValueChange = {},
                            label = "Sobre mí",
                            textColor = Color.Black,
                            isEditable = false,
                            backgroundColor = Bone,
                            isRound = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Información sobre el perfil
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

                // Sección "Más sobre mí"
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

                // Sección "Estilo de vida"
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

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botones para acciones
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                                showReportModal = true // Cambiar a mostrar el modal de denuncia
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Denunciar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                onBlock()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Bloquear", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Mostrar el modal de denuncia si showReportModal es true
    if (showReportModal) {
        ReportModal(
            onDismiss = { showReportModal = false; onClose() },
            onReasonChange = { reason -> selectedReason = reason },
            onDetailChange = { detail -> selectedDetail = detail },
            onExtraDetailsChange = { extra -> extraDetails = extra },
            onReport = {
                onReport(
                    selectedReason,
                    selectedDetail,
                    extraDetails
                )
            }
        )
    }
}

@Composable
fun ReportModal(
    onDismiss: () -> Unit,
    onReasonChange: (String) -> Unit,
    onDetailChange: (String) -> Unit,
    onExtraDetailsChange: (String) -> Unit,
    onReport: () -> Unit
) {
    val navController = rememberNavController()
    var extraDetails by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ReportProgressBar(currentStep = navController.currentBackStackEntry?.destination?.route?.let {
                getCurrentStep(it)
            } ?: 0)

            NavHost(navController = navController, startDestination = "reason") {
                composable("reason") {
                    ReasonScreen(
                        onNext = { reason ->
                            onReasonChange(reason) // Notificar el cambio
                            navController.navigate("details/$reason")
                        },
                        onDismiss = onDismiss
                    )
                }
                composable("details/{reason}") { backStackEntry ->
                    val reason = backStackEntry.arguments?.getString("reason") ?: ""
                    DetailsScreen(
                        reason = reason,
                        onNext = { detail ->
                            onDetailChange(detail) // Notificar el cambio
                            navController.navigate("send")
                        },
                        onBack = {
                            navController.popBackStack() // Volver a "reason"
                        }
                    )
                }
                composable("send") {
                    SendScreen(
                        extraDetails = extraDetails,
                        onExtraDetailsChange = { extraDetails = it
                            onExtraDetailsChange(it) // Notificar el cambio
                        },
                        onSubmit = {
                            // Llamar a onReport con los datos recopilados
                            onReport()
                            navController.popBackStack(
                                "reason",
                                inclusive = false
                            ) // Volver a "reason"
                        },
                        onBack = {
                            navController.popBackStack() // Volver a "details"
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ReasonScreen(onNext: (String) -> Unit, onDismiss: () -> Unit) {
    val reasons = LocalContext.current.resources.getStringArray(R.array.report_reasons)
    var selectedReason by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "¿Por qué quieres denunciar este perfil?",
            style = MaterialTheme.typography.titleMedium
        )

        // Crear una lista de opciones
        reasons.forEach { reason ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (selectedReason == reason),
                    onClick = { selectedReason = reason }
                )
                Text(
                    reason,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        // Spacer para empujar los botones hacia abajo
        Spacer(modifier = Modifier.weight(1f)) // Ocupa el espacio restante

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cerrar", color = Color.White)
            }

            Button(
                onClick = { if (selectedReason.isNotEmpty()) onNext(selectedReason) },
                enabled = selectedReason.isNotEmpty(), // Deshabilitar si no hay opción seleccionada
                colors = ButtonDefaults.buttonColors(containerColor = MainColor)
            ) {
                Text("Siguiente", color = Color.White)
            }
        }
    }
}

@Composable
fun DetailsScreen(reason: String, onNext: (String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val detailsOptions = when (reason) {
        "Spam" -> context.resources.getStringArray(R.array.details_options_spam)
        "Inappropriate Content" -> context.resources.getStringArray(R.array.details_options_inappropriate_content)
        "Harassment" -> context.resources.getStringArray(R.array.details_options_harassment)
        "Other" -> context.resources.getStringArray(R.array.details_options_other)
        else -> emptyArray()
    }

    var selectedDetail by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Selecciona más detalles sobre tu denuncia",
            style = MaterialTheme.typography.titleMedium
        )

        // Crear una lista de opciones
        detailsOptions.forEach { detail ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { selectedDetail = detail }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (selectedDetail == detail),
                    onClick = { selectedDetail = detail }
                )
                Text(
                    detail,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        // Spacer para empujar los botones hacia abajo
        Spacer(modifier = Modifier.weight(1f)) // Ocupa el espacio restante

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar", color = Color.White)
            }

            Button(
                onClick = { if (selectedDetail.isNotEmpty()) onNext(selectedDetail) },
                enabled = selectedDetail.isNotEmpty(), // Deshabilitar si no hay opción seleccionada
                colors = ButtonDefaults.buttonColors(containerColor = MainColor)
            ) {
                Text("Siguiente", color = Color.White)
            }
        }
    }
}

@Composable
fun SendScreen(
    extraDetails: String,
    onExtraDetailsChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Revisa y envía tu denuncia")

        // Campo para detalles adicionales
        TextField(
            value = extraDetails,
            onValueChange = onExtraDetailsChange,
            label = { Text("Detalles adicionales") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Volver", color = Color.White)
            }

            Button(onClick = {
                onSubmit() // Aquí se envían todos los datos
            }, colors = ButtonDefaults.buttonColors(containerColor = MainColor)) {
                Text("Enviar", color = Color.White)
            }
        }
    }
}

@Composable
fun ReportProgressBar(currentStep: Int) {
    val colors = List(3) { Color.Gray }.toMutableList()
    if (currentStep in colors.indices) colors[currentStep] = MainColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reportar Perfil", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            colors.forEachIndexed { index, color ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .background(color)
                    )

                    Text(
                        when (index) {
                            0 -> "Razón"
                            1 -> "Detalles"
                            2 -> "Enviar"
                            else -> ""
                        },
                        color = if (currentStep == index) MainColor else Color.Gray
                    )
                }

                if (index < colors.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

// Añade la función para obtener el paso actual
fun getCurrentStep(route: String?): Int {
    return when (route) {
        "reason" -> 0
        "details/{reason}" -> 1
        "send" -> 2
        else -> 0
    }
}
