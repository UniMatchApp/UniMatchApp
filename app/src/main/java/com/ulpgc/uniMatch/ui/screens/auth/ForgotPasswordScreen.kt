package com.ulpgc.uniMatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R

@Composable
fun ForgotPasswordScreen(onSubmit: (String) -> Unit) {
    var emailOrPhone by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Ilustración
        Icon(
            painter = painterResource(id = R.drawable.ep_question_filled), // Tu drawable aquí
            contentDescription = null,
            modifier = Modifier.size(150.dp),
            tint = MaterialTheme.colorScheme.primary // Cambia dinámicamente el color según el tema
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Descripción
        Text(
            text = "Don't worry! It happens. Please enter the address associated with your account.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Campo de entrada
        BasicTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = Color.White,
                    shape = MaterialTheme.shapes.small
                )
                .padding(16.dp),
            decorationBox = { innerTextField ->
                if (emailOrPhone.text.isEmpty()) {
                    Text("Email ID / Mobile number", color = Color.Gray)
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón "Submit"
        Button(
            onClick = { onSubmit(emailOrPhone.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
        ) {
            Text(text = "Submit", fontSize = 16.sp)
        }
    }
}
