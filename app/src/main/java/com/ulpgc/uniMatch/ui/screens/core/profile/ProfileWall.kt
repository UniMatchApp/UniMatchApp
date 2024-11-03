package com.ulpgc.uniMatch.ui.screens.core.profile


import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.WallGrid
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar


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

            val activity = LocalContext.current as? ComponentActivity // Obtener la actividad actual

            WallGrid(activity!!, initialProfileImages = profileImages, onAddImageClick = onAddImageClick) // Añadido el parámetro onAddImageClick

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