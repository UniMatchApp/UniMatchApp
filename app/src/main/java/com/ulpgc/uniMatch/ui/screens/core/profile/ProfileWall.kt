package com.ulpgc.uniMatch.ui.screens.core.profile


import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.profile.WallGridDraggable


@Composable
fun ProfileWall(
    profileViewModel: ProfileViewModel,
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profile = profileViewModel.profileData.collectAsState().value
    val activity = LocalContext.current as? ComponentActivity


     if( profile != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            WallGridDraggable(
                activity!!,
                initialProfileImages = profile.wall,
                onAddImageClick = { imageUrl ->
                    profileViewModel.addImage(imageUrl)
                },
                onDeleteImageClick = { imageUrl ->
                    profileViewModel.deleteImage(imageUrl)
                },
                onUpdateWallOrder = { newOrder ->
                    profileViewModel.updateWall(newOrder)
                },
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.loading_error))
        }
    }

}

