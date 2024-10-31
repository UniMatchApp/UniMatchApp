package com.ulpgc.uniMatch.ui.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun WallGrid(profileImages: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val totalImages = profileImages.size
        val totalSlots = 12
        val emptySlots = totalSlots - totalImages

        items(profileImages) { imageUri ->
            Surface(
                modifier = Modifier
                    .size(width = 100.dp, height = 160.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Image(
                    painter = rememberImagePainter(imageUri),
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
                        .size(width = 100.dp, height = 140.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.LightGray
                ) {

                }
            }
        }

    }
}
