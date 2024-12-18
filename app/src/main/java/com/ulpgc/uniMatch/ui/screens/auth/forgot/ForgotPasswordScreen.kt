package com.ulpgc.uniMatch.ui.screens.auth.forgot

import android.util.Log
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
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.ButtonComponent

@Composable
fun ForgotPasswordScreen(
    userViewModel: UserViewModel,
    errorViewModel: ErrorViewModel,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val forgotPasswordResult = userViewModel.forgotPasswordResult.collectAsState()
    val resetPasswordFailed = stringResource(R.string.reset_password_failed)

    LaunchedEffect(forgotPasswordResult.value) {
        if (forgotPasswordResult.value != null) {
            if (forgotPasswordResult.value!!) {
                onSubmit()
            }
        }
    }

    BackHandler { onBack() }

    ForgotPasswordContent(
        onSubmit = { email ->
            if (email.isEmpty()) {
                errorViewModel.showError(resetPasswordFailed)
            } else {
                userViewModel.forgotPassword(email)
            }
        },
        onBack = { onBack() }
    )
}


@Composable
fun ForgotPasswordContent(
    onSubmit: (String) -> Unit,
    onBack: () -> Unit
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

        ButtonComponent(
            onClick = { onSubmit(emailOrPhone.text) },
            text = stringResource(R.string.submit),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonComponent(
            onClick = { onBack() },
            text = stringResource(R.string.back),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
