package com.ulpgc.uniMatch.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LegalSection(
    onCookiesClick : () -> Unit,
    onPrivacyClick : () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
    ) {
        LegalOptionButton("Política de cookies", onCookiesClick)
        LegalOptionButton("Política de privacidad", onPrivacyClick)
        Button(
            onClick = { /* Acción de eliminar cuenta */ },
            colors = ButtonDefaults.buttonColors(Color(0xFFD7A2C3)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Eliminar cuenta", color = Color.White)
        }
    }
}

@Composable
fun LegalOptionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = text, color = Color.Black)
    }
}

