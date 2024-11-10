package com.ulpgc.uniMatch.ui.screens.auth.register

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.components.ButtonComponent
import com.ulpgc.uniMatch.ui.components.InputField

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    continueRegister: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableIntStateOf(0) }
    val registeredUserId by authViewModel.registeredUserId.collectAsState()

    LaunchedEffect(registeredUserId) {
        registeredUserId?.let {
            continueRegister.invoke(
                it
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 30.dp)
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
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = stringResource(R.string.enter_your_details_to_register),
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
            Spacer(modifier = Modifier.height(8.dp))
            InputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = stringResource(R.string.confirm_password),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ButtonComponent(
                    onClick = onBackClick,
                    text = stringResource(R.string.back),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                ButtonComponent(
                    onClick = {
                        if (password != confirmPassword) {
                            showErrorDialog = true
                            errorMessage = R.string.passwords_do_not_match
                        } else if (password.isEmpty() || email.isEmpty()) {
                            showErrorDialog = true
                            errorMessage = R.string.fields_empty_error
                        } else {
                            authViewModel.register(email, password)
                        }
                    },
                    text = stringResource(R.string.register_button),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.already_have_an_account), color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.login)),
                    onClick = { onLoginClick() },
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = stringResource(R.string.registration_failed)) },
            text = { Text(text = stringResource(errorMessage)) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}
