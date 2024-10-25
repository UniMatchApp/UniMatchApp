package com.ulpgc.uniMatch.ui.components


import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonComponent(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium // Utiliza el `shape` definido en el tema


    ) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}
