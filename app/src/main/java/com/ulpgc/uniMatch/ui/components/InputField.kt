package com.ulpgc.uniMatch.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
    isValid: (String) -> Boolean = { true },
    textColor: Color = Color.Black,
    isEditable: Boolean = true,
    backgroundColor: Color = Color.White,
    isRound: Boolean = false,
    border: Boolean = true
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isFieldValid = isValid(value)
    val borderColor =
        if (isFieldValid) MaterialTheme.colorScheme.onSurface else Color.Red

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                if (border) BorderStroke(1.dp, borderColor) else BorderStroke(
                    0.dp,
                    Color.Transparent
                ),
                if (isRound) RoundedCornerShape(8.dp) else MaterialTheme.shapes.small
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    backgroundColor,
                    if (isRound) RoundedCornerShape(8.dp) else MaterialTheme.shapes.small
                )
                .padding(10.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .let { if (isEditable) it.clickable { } else it },
                textStyle = TextStyle(color = textColor),
                visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                enabled = isEditable,
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.weight(1f)) {
                            if (value.isEmpty()) Text(
                                label,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onTertiary.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
                            innerTextField()
                        }

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
}
