package com.ulpgc.uniMatch.ui.screens.auth

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.ui.screens.AuthRoutes

@Composable
fun VerifyCodeScreen(
    authViewModel: AuthViewModel,
    userId: String,
    onVerificationSuccess: () -> Unit = {},
    onBack: () -> Unit
) {

    val verifyCodeResult = authViewModel.verifyCodeResult.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableIntStateOf(0) }

    LaunchedEffect (verifyCodeResult.value) {
        if (verifyCodeResult.value != null) {
            if (verifyCodeResult.value!!) {
                onVerificationSuccess()
            }
            authViewModel.resetVerificationResult()
        }
    }

    BackHandler {
        onBack()
    }

    VerifyCodeContent(
        onCodeSubmit = { code ->
            Log.d("VerifyCodeScreen", "Code: $code")
            if (code.length != 6) {
                errorMessage = R.string.please_enter_valid_code
                showErrorDialog = true
            } else if (code.any { !it.isDigit() }) {
                errorMessage = R.string.please_enter_valid_code
                showErrorDialog = true
            } else {
                authViewModel.verifyCode(userId, code)
            }
        },
        onBack = { onBack() },
        showErrorDialog = showErrorDialog,
        errorMessage = errorMessage,
        onDismissErrorDialog = { showErrorDialog = false }
    )
}

@Composable
fun VerifyCodeContent(
    onCodeSubmit: (String) -> Unit,
    onBack: () -> Unit,
    showErrorDialog: Boolean,
    errorMessage: Int,
    onDismissErrorDialog: () -> Unit
) {
    var code by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = List(6) { FocusRequester() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.verify_code),
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.introduce_code),
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            code.forEachIndexed { index, digit ->
                BasicTextField(
                    value = digit,
                    onValueChange = { input ->
                        if (input.length == 1 && input[0].isDigit()) {
                            code = code.toMutableList().apply { this[index] = input }
                            if (index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        } else if (input.isEmpty()) {
                            code = code.toMutableList().apply { this[index] = "" }
                        }
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = Color.White,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(8.dp)
                        .focusRequester(focusRequesters[index]),
                    decorationBox = { innerTextField ->
                        if (digit.isEmpty()) {
                            Text("0", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onCodeSubmit(code.joinToString(""))
            },
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
            title = { Text("Error") },
            text = { Text(stringResource(id = errorMessage)) },
            confirmButton = {
                Button(onClick = { onDismissErrorDialog() }) {
                    Text("OK")
                }
            }
        )
    }
}
