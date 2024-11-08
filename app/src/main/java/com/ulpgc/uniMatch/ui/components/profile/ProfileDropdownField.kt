package com.ulpgc.uniMatch.ui.components.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.ui.components.DropdownMenu

@Composable
fun ProfileDropdownField(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onEditField: (String) -> Unit,
    includeNullOption: Boolean = false
) {
    var currentSelection by remember { mutableStateOf(selectedOption.takeIf { it in options } ?: options.first()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        DropdownMenu(
            options,
            currentSelection,
            onItemSelected = { selected ->
                currentSelection = selected ?: options.first()
                onEditField(currentSelection)
            },
            includeNullOption
        )
    }
}


