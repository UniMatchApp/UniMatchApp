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
fun ProfileOptionRow(type: String, option: String, isSelectable: Boolean = true) {

    val context = LocalContext.current
    val iconName = "icon_${type.lowercase()}"
    val title = context.resources.getString(context.resources.getIdentifier(type, "string", context.packageName))
    val iconId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    val listId = context.resources.getIdentifier(type, "array", context.packageName) // Obtenemos el ID del array de strings

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

        DropdownMenuShorter(items = options, selectedItem = option, isSelectable = isSelectable)
    }
}