package com.ulpgc.uniMatch.ui.screens.core.profile


import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.WallGrid


@Composable
fun ProfileWall(
    profileViewModel: ProfileViewModel,
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
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.loading_error))
        }
    }

}

