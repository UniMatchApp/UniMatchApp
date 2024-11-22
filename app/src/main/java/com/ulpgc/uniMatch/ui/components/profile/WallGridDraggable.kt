package com.ulpgc.uniMatch.ui.components.profile

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.burnoutcrew.reorderable.NoDragCancelledAnimation
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.theme.MainColor
import org.burnoutcrew.reorderable.detectReorderAfterLongPress

@Composable
fun WallGridDraggable(
    activity: Activity,
    initialProfileImages: List<String>,
    onAddImageClick: (Uri) -> Unit,
    onDeleteImageClick: (String) -> Unit,
    onUpdateWallOrder: (List<String>) -> Unit
) {

    val profileImages = remember { mutableStateListOf(*initialProfileImages.toTypedArray()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri: Uri? = data?.data
            imageUri?.let {
                if (profileImages.size < 9) {
                    profileImages.add(it.toString())
                    Log.i("WallGrid", "Added image: $profileImages")
                    onAddImageClick(it)
                }
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val imgHeight = (screenHeight - 56 - 16 - 32 ).div(3).dp // 56dp = AppBar , 16dp = padding, 32dp = 2*16dp de padding entre ellos

    val state = rememberReorderableLazyGridState(dragCancelledAnimation = NoDragCancelledAnimation(),
        onMove = { from, to ->
            profileImages.add(to.index, profileImages.removeAt(from.index))
            onUpdateWallOrder(profileImages)
        }
    )



    Box(
        modifier = Modifier
            .fillMaxSize().padding(8.dp)
    ) {
        // Grid de imágenes
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = state.gridState,
            modifier = Modifier
                .reorderable(state)
                .fillMaxSize()
        ) {
            items(profileImages, { it }) { item ->
                ReorderableItem(state, key = item) { isDragging ->
                    Log.i("WallGrid", "Item: $isDragging")
                    Box(
                        modifier = Modifier
                            .height(imgHeight)
                            .fillMaxSize()
                            .padding(4.dp)
                            .reorderable(state)
                            .detectReorderAfterLongPress(state)
                    ) {
                        if (item.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item)
                                        .build(),
                                    contentDescription = "Imagen de perfil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.White, CircleShape)
                                    .border(2.dp, Color.Black, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        profileImages.remove(item)
                                        onDeleteImageClick(item)
                                    }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_remove),
                                    contentDescription = "Eliminar imagen",
                                    tint = Color.Red,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botón de añadir imagen
        if (profileImages.size != 9) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)  // Alinea el botón en la esquina inferior derecha
                    .background(MainColor, CircleShape),  // Fondo circular

                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_add_photo),
                    contentDescription = "Añadir imagen",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize().padding(4.dp)
                )
            }
        }
    }



    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.upload_image)) },
            text = { Text(stringResource(R.string.where_do_you_wanna_get_image)) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    ImagePicker.with(activity)
                        .cameraOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent -> imagePickerLauncher.launch(intent) }
                }) {
                    Text(stringResource(R.string.camera))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    ImagePicker.with(activity)
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent -> imagePickerLauncher.launch(intent) }
                }) {
                    Text(stringResource(R.string.gallery))
                }
            }
        )
    }
}
