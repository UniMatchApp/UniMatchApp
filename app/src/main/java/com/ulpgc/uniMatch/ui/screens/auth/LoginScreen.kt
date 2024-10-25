package com.ulpgc.uniMatch.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.components.ButtonComponent
import com.ulpgc.uniMatch.ui.components.InputField


@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 30.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = R.drawable.unimatch_logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(125.dp)
            )
            Text(
                text = stringResource(R.string.sign_in_to_unimatch),
                style = MaterialTheme.typography.headlineLarge
            )

        }

        Spacer(modifier = Modifier.height(8.dp))
        Column {
            Text(
                text = stringResource(R.string.enter_your_email_and_password_to_log_in),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
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

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            ButtonComponent(
                onClick = { onLoginClick(email, password) },
                text = stringResource(R.string.login_button),
                modifier = Modifier.fillMaxWidth()
            )

            // Forgot password link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { rememberMe = !rememberMe }) {
                    Checkbox(checked = rememberMe, onCheckedChange = {})
                    Text(text = stringResource(R.string.remember_me), fontSize = 14.sp)
                }
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.forgot_password)),
                    onClick = { onForgotPasswordClick() },
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // "Don't have an account?" with Sign Up link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account?", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            ClickableText(
                text = AnnotatedString("Sign Up"),
                onClick = { onSignUpClick() },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
