package com.ulpgc.uniMatch.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorDialog(errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss, // Cerrar el dialogo
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK", color = Color.White)
            }
        },
        title = {
            Text(text = "Error")
        },
        text = {
            Text(text = errorMessage)
        }
    )
}
