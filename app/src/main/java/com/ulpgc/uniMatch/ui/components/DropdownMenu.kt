package com.ulpgc.uniMatch.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var selectedText by remember { mutableStateOf(selectedItem) }
    var defaultText = stringResource(id = R.string.none)

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            (if (selectedText in items) selectedText else defaultText)?.let {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    value = it,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    )
            }


            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                if (includeNullOption) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.dropdown_empty_option)) },
                        onClick = {
                            selectedText = null
                            isExpanded = false
                            onItemSelected(null)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }

                items.forEach { text ->
                    DropdownMenuItem(
                        text = { Text(text = text, color = MaterialTheme.colorScheme.onBackground  ) },
                        onClick = {
                            selectedText = text
                            isExpanded = false
                            onItemSelected(text)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,

                    )
                }
            }
        }
    }
}
