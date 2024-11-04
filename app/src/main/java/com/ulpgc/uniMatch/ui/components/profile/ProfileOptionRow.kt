package com.ulpgc.uniMatch.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun ProfileOptionRow(
    type: String,
    option: String,
    isSelectable: Boolean = true,
    onSelectedItemChange: (String) -> Unit
) {

    val context = LocalContext.current
    val iconName = "icon_${type.lowercase()}"
    val title = context.resources.getString(
        context.resources.getIdentifier(
            type,
            "string",
            context.packageName
        )
    )
    val iconId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    val listId = context.resources.getIdentifier(type, "array", context.packageName)

    val options = if (listId != 0) {
        context.resources.getStringArray(listId).toList()
    } else {
        listOf("Sin opciones disponibles")
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            if (iconId != 0) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "Icono de usuario",
                    tint = Color.DarkGray,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icono por defecto",
                    tint = Color.DarkGray,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = title, color = Color.DarkGray, fontSize = 12.sp)

            Spacer(modifier = Modifier.width(8.dp))

        }

        DropdownMenuShorter(
            items = options,
            selectedItem = option,
            isSelectable = isSelectable,
            onSelectedItemChange = { selectedItem ->
                onSelectedItemChange(selectedItem)
            }
        )
    }
}