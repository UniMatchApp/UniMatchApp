package com.ulpgc.uniMatch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PasswordVisibilityIcon(isVisible: Boolean, onClick: () -> Unit) {
    Icon(
        imageVector = if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
        contentDescription = if (isVisible) "Hide password" else "Show password",
        modifier = Modifier.clickable(onClick = onClick),
        tint = Color.Black
    )
}