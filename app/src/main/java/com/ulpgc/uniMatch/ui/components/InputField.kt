package com.ulpgc.uniMatch.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isValid: (String) -> Boolean = { true }, // Función de validación
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    var isPasswordVisible by remember { mutableStateOf(false) } // Controlar si se muestra la contraseña o no
    val isFieldValid = isValid(value) // Validar el campo con la función proporcionada
    val borderColor =
        if (isFieldValid) MaterialTheme.colorScheme.onSurface else Color.Red // Color del borde basado en validez

    Box(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                // Cambiar el color del borde según validez
                .border(1.dp, borderColor, MaterialTheme.shapes.small)
                .padding(10.dp),
            textStyle = TextStyle(color = textColor),
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty()) Text(
                            label,
                            style = TextStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        )
                        innerTextField() // Campo de texto
                    }

                    // Usa el PasswordVisibilityIcon en lugar de Image
                    if (isPassword) {
                        PasswordVisibilityIcon(
                            isVisible = isPasswordVisible,
                            onClick = { isPasswordVisible = !isPasswordVisible }
                        )
                    }
                }
            }
        )
    }
}

