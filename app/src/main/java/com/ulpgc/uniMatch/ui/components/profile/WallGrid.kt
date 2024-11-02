package com.ulpgc.uniMatch.ui.components.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import coil.request.ImageRequest

@Composable
fun WallGrid(initialProfileImages: List<String>, onAddImageClick: () -> Unit) {

    var showDialog by remember { mutableStateOf(false) }
    val profileImages = remember { mutableStateListOf(*initialProfileImages.toTypedArray()) } // SnapshotStateList

    // ActivityResultLauncher para seleccionar un archivo de imágenes
    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.i("WallGrid", "URI: $it")
            profileImages.add(it.toString()) // Agrega la imagen sin recomponer todo
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val totalImages = profileImages.size
        val totalSlots = 9
        val emptySlots = totalSlots - totalImages

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

        repeat(emptySlots) {
            item {
                Surface(
                    modifier = Modifier
                        .size(width = 100.dp, height = 160.dp)
                        .clickable { showDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.LightGray
                ) {
                    // Panel vacío que abrirá el diálogo de selección
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

                }) {
                    Text("Cámara")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    filePickerLauncher.launch("image/*")
                }) {
                    Text("Archivos")
                }
            }
        )
    }
}