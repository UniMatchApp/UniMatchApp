package com.ulpgc.uniMatch.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.theme.MainColor

@Composable
fun InterestGrid(initialInterestList: List<String>) {
    // Usamos remember para mantener la lista de intereses seleccionados
    val selectedInterests = remember { mutableStateListOf<String>().apply { addAll(initialInterestList) } }

    val context = LocalContext.current
    val interests = context.resources.getStringArray(R.array.interests).toList()
    val combinedList = interests + selectedInterests // Combina la lista de intereses

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(combinedList.distinct(), key = { it }) { interest ->
            Box(
                modifier = Modifier
                    .background(
                        if (interest in selectedInterests) MainColor else Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    ) // Fondo redondeado
                    .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)) // Bordes redondeados
                    .padding(8.dp) // Padding interno
                    .clickable {
                        if (interest !in selectedInterests) {
                            selectedInterests.add(interest)
                        } else {
                            selectedInterests.remove(interest)
                        }
                    }
            ) {
                Text(
                    text = interest,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center) // Centrar el texto
                )
            }
        }
    }
}
