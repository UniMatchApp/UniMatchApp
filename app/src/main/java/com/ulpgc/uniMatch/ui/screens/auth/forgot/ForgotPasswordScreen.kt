package com.ulpgc.uniMatch.ui.screens.auth.forgot

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.screens.AuthRoutes

@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val forgotPasswordResult = authViewModel.forgotPasswordResult.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect (forgotPasswordResult.value) {
        if (forgotPasswordResult.value != null) {
            if (forgotPasswordResult.value!!) {
                onSubmit()
            }
            authViewModel.resetForgotPasswordResult()
        }
    }

    BackHandler { onBack() }

    ForgotPasswordContent(
        onSubmit = { email ->
            if (email.isEmpty()) {
                showErrorDialog = true
            } else{
                authViewModel.forgotPassword(email)
            }
        },
        onBack = { onBack() },
        showErrorDialog = showErrorDialog,
        onDismissErrorDialog = { showErrorDialog = false }
    )
}


@Composable
fun ForgotPasswordContent(
    onSubmit: (String) -> Unit,
    onBack: () -> Unit,
    showErrorDialog: Boolean,
    onDismissErrorDialog: () -> Unit
) {
    var emailOrPhone by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ep_question_filled),
            contentDescription = null,
            modifier = Modifier.size(150.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.forgot_password_cheers),
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

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
                    Text(stringResource(R.string.enter_email), color = Color.Gray)
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSubmit(emailOrPhone.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
        ) {
            Text(text = stringResource(R.string.submit), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onBack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
        ) {
            Text(text = stringResource(R.string.back), fontSize = 16.sp)
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { onDismissErrorDialog() },
            title = { Text(stringResource(R.string.error_title)) },
            text = { Text(stringResource(R.string.reset_password_failed)) },
            confirmButton = {
                Button(onClick = { onDismissErrorDialog() }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

}
