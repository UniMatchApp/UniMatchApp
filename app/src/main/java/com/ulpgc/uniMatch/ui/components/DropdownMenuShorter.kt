package com.ulpgc.uniMatch.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuShorter(
    items: List<String>,
    selectedItem: String,
    isSelectable: Boolean = true,
    onSelectedItemChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val defaultText = stringResource(R.string.none)
    var selectedText by remember { mutableStateOf(selectedItem) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                if (isSelectable) isExpanded = !isExpanded
            },
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.End)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .menuAnchor(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if(selectedText in items) selectedText else defaultText,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                )

                if (isSelectable) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { text ->
                    DropdownMenuItem(
                        text = { Text(text = text, fontSize = 12.sp) },
                        onClick = {
                            selectedText = text
                            isExpanded = false
                            onSelectedItemChange(text)
                        }
                    )
                }
            }
        }
    }
}
