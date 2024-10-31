package com.ulpgc.uniMatch.ui.screens.core.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import coil.compose.AsyncImage
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.components.profile.WallGrid


@Composable
fun ProfileWall(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    onAddImageClick: () -> Unit // Añadido el parámetro onAddImageClick
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profile = profileViewModel.profileData.collectAsState().value
    val profileImages = profile?.wall ?: emptyList()

    Column {
        ProfileSettingsTopBar { navController.popBackStack() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            WallGrid(profileImages = profileImages)

            Spacer(modifier = Modifier.height(16.dp))


            Text(text = "Datos de sesión", style = MaterialTheme.typography.titleLarge)
            Button(
                onClick = { /* Modificar correo electrónico */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Modificar correo electrónico")
            }

            Button(
                onClick = { /* Modificar contraseña */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Modificar contraseña")
            }
        }
    }
}