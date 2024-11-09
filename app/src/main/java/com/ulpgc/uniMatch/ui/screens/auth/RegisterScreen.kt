package com.ulpgc.uniMatch.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.components.ButtonComponent
import com.ulpgc.uniMatch.ui.components.InputField

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column {
        Text(text = stringResource(R.string.register), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        InputField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.enter_email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        InputField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.enter_password),
            isPassword = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        InputField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = stringResource(R.string.confirm_password),
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonComponent(
            onClick = {
                // Call the register function with the email and password
                authViewModel.register(email, password)
            },
            text = stringResource(R.string.register_button),
            modifier = Modifier.fillMaxWidth()

        )

        // Button to go back
        ButtonComponent(
            onClick = onBack,
            text = stringResource(R.string.cancel)
        )
    }
}
