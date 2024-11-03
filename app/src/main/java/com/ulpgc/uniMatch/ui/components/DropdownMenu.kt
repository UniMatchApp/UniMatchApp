package com.ulpgc.uniMatch.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ulpgc.uniMatch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String?) -> Unit = {},
    includeNullOption: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedItem) } // Ahora puede ser null

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                value = selectedText ?: stringResource(id = R.string.dropdown_empty_option), // Mostrar texto vacío si es null
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }
            )

            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                // Agregar un item vacío si se desea
                if (includeNullOption) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.dropdown_empty_option)) },
                        onClick = {
                            selectedText = null // Asignar null cuando se selecciona la opción vacía
                            isExpanded = false
                            onItemSelected(null) // Llamada a onItemSelected con null
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }

                items.forEach { text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            selectedText = text
                            isExpanded = false
                            onItemSelected(text)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}
