package com.ulpgc.uniMatch.ui.components


import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ulpgc.uniMatch.ui.theme.White

@Composable
fun ButtonComponent(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(color)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = White
        )
    }
}
