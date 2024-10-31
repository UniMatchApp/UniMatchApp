package com.ulpgc.uniMatch.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.ui.components.DropdownMenuShorter

@Composable
fun ProfileOptionRow(title: String, option: String) {
    val traducciones = mapOf(
        "Horóscopo" to "horoscopes",
        "Educación" to "school",
        "Tipo de personalidad" to "personality_type",
        "Mascotas" to "pets",
        "¿Bebes?" to "drinks",
        "¿Fumas?" to "smokes",
        "¿Haces deporte?" to "sports",
        "Valores y creencias" to "religion"
    )

    val context = LocalContext.current
    val iconName = "icon_${traducciones[title] ?: "default"}"
    val iconId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    val listName = traducciones[title] ?: "default"
    val listId = context.resources.getIdentifier(listName, "array", context.packageName) // Obtenemos el ID del array de strings

    val options = if (listId != 0) {
        context.resources.getStringArray(listId).toList()
    } else {
        listOf("Sin opciones disponibles")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            if (iconId != 0) { // Verificamos que iconId sea válido
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "Icono de usuario",
                    tint = Color.DarkGray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person, // Icono por defecto
                    contentDescription = "Icono por defecto",
                    tint = Color.DarkGray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(text = title, color = Color.DarkGray, fontSize = 12.sp)
        }

        DropdownMenuShorter(items = options, selectedItem = option)
    }
}