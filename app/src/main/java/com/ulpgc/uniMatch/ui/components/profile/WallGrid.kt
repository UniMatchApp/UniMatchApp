package com.ulpgc.uniMatch.ui.components.profile

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ulpgc.uniMatch.R

@Composable
fun WallGrid(
    activity: Activity,
    initialProfileImages: List<String>,
    onAddImageClick: (String) -> Unit,
    onDeleteImageClick: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val profileImages = remember { mutableStateListOf(*initialProfileImages.toTypedArray()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri: Uri? = data?.data
            imageUri?.let {
                if (profileImages.size < 9) {
                    profileImages.add(it.toString())
                    onAddImageClick(it.toString())
                }
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(profileImages) { imageUri ->
            Log.i("WallGrid", "Imagen: $imageUri")
            Box(
                modifier = Modifier.size(width = 100.dp, height = 180.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxSize().padding(4.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUri)
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
                            profileImages.remove(imageUri)
                            onDeleteImageClick(imageUri)
                        }
                        .padding(4.dp)
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

        if (profileImages.size < 9) {
            repeat(9 - profileImages.size) { item ->
                item {
                    Surface(
                        modifier = Modifier
                            .size(width = 100.dp, height = 180.dp)
                            .clickable { showDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        color = Color.LightGray
                    ) {
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Subir archivo") },
            text = { Text("¿Desde dónde quieres subir el archivo?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    ImagePicker.with(activity)
                        .cameraOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent -> imagePickerLauncher.launch(intent) }
                }) {
                    Text("Cámara")
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
                    Text("Archivos")
                }
            }
        )
    }
}
