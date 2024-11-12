package com.ulpgc.uniMatch.ui.screens.core.profile


import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.WallGrid
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar


@Composable
fun ProfileWall(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profile = profileViewModel.profileData.collectAsState().value
    val profileImages = profile?.wall ?: emptyList()
    val activity = LocalContext.current as? ComponentActivity
    val isLoading by profileViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if( profile != null) {
        Column(
        ) {

            ProfileSettingsTopBar { navController.popBackStack() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                WallGrid(
                    activity!!,
                    initialProfileImages = profileImages,
                    onAddImageClick = { imageUrl ->
                        profileViewModel.addImage(imageUrl)
                    },
                    onDeleteImageClick = { imageUrl ->
                        profileViewModel.deleteImage(imageUrl)
                    }
                )

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
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.loading_error))
        }
    }

}

