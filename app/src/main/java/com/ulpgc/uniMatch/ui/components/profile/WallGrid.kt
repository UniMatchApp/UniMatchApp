package com.ulpgc.uniMatch.ui.components.profile

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.dhaval2404.imagepicker.ImagePicker

@Composable
fun WallGrid(
    activity: Activity, // Agregar parámetro de Activity
    initialProfileImages: List<String>,
    onAddImageClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val profileImages = remember { mutableStateListOf(*initialProfileImages.toTypedArray()) }

    // Lanzador de resultado para la cámara y la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri: Uri? = data?.data // Obtener la URI de la imagen
            imageUri?.let {
                profileImages.add(it.toString()) // Añadir URI a la lista
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
            Surface(
                modifier = Modifier.size(width = 100.dp, height = 160.dp),
                shape = RoundedCornerShape(16.dp)
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
        }

        // Espacio para añadir imágenes
        item {
            Surface(
                modifier = Modifier
                    .size(width = 100.dp, height = 160.dp)
                    .clickable { showDialog = true },
                shape = RoundedCornerShape(16.dp),
                color = Color.LightGray
            ) {
                // Puedes añadir un contenido aquí para el espacio de añadir imágenes, como un icono.
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
                    // Usar ImagePicker para tomar foto con la cámara
                    ImagePicker.with(activity)
                        .cameraOnly()
                        .compress(1024) // Opcional: comprimir imagen
                        .maxResultSize(1080, 1080) // Opcional: tamaño máximo de la imagen
                        .createIntent { intent -> imagePickerLauncher.launch(intent) }
                }) {
                    Text("Cámara")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    // Usar ImagePicker para seleccionar desde la galería
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

