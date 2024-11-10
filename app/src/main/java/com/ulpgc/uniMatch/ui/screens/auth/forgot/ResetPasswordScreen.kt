package com.ulpgc.uniMatch.ui.screens.auth.forgot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.components.InputField

@Composable
fun ResetPasswordScreen(
    authViewModel: AuthViewModel,
    userId: String,
    onPasswordReset: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(0) }

    val resultPasswordReset by authViewModel.resetPasswordResult.collectAsState()

    LaunchedEffect(resultPasswordReset) {
        if (resultPasswordReset != null) {
            if (resultPasswordReset!!) {
                onPasswordReset()
            }
            authViewModel.resetPasswordResult()
        }
    }

    val onSubmitPassword = {
        if (newPassword.length < 6) {
            errorMessage = R.string.password_too_short
            showErrorDialog = true
        } else if (newPassword != confirmPassword) {
            errorMessage = R.string.passwords_do_not_match
            showErrorDialog = true
        } else {
            authViewModel.resetPassword(userId, newPassword)
        }
    }

    ResetPasswordContent(
        newPassword = newPassword,
        confirmPassword = confirmPassword,
        onNewPasswordChange = { newPassword = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onSubmitPassword = onSubmitPassword,
        showErrorDialog = showErrorDialog,
        errorMessage = errorMessage,
        onDismissErrorDialog = { showErrorDialog = false }
    )
}

@Composable
fun ResetPasswordContent(
    newPassword: String,
    confirmPassword: String,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmitPassword: () -> Unit,
    showErrorDialog: Boolean,
    errorMessage: Int,
    onDismissErrorDialog: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.reset_password),
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        InputField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            label = stringResource(R.string.new_password),
            isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = stringResource(R.string.confirm_password),
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSubmitPassword,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
        ) {
            Text(text = stringResource(R.string.submit), fontSize = 16.sp)
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { onDismissErrorDialog() },
            title = { Text(stringResource(R.string.error_title)) },
            text = { Text(stringResource(id = errorMessage)) },
            confirmButton = {
                Button(onClick = { onDismissErrorDialog() }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}
