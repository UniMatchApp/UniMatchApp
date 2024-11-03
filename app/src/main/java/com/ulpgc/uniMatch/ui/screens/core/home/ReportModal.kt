package com.ulpgc.uniMatch.ui.screens.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulpgc.uniMatch.R

@Composable
fun ReportModal(
    onDismiss: () -> Unit,
    onReasonChange: (String) -> Unit,
    onDetailChange: (String) -> Unit,
    onExtraDetailsChange: (String) -> Unit,
    onReport: () -> Unit
) {
    val navController = rememberNavController()
    var extraDetails by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ReportProgressBar(currentStep = navController.currentBackStackEntry?.destination?.route?.let {
                getCurrentStep(it)
            } ?: 0)

            NavHost(navController = navController, startDestination = "reason") {
                composable("reason") {
                    ReasonScreen(
                        onNext = { reason ->
                            onReasonChange(reason)
                            navController.navigate("details/$reason")
                        },
                        onDismiss = onDismiss
                    )
                }
                composable("details/{reason}") { backStackEntry ->
                    val reason = backStackEntry.arguments?.getString("reason") ?: ""
                    DetailsScreen(
                        reason = reason,
                        onNext = { detail ->
                            onDetailChange(detail)
                            navController.navigate("send")
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("send") {
                    SendScreen(
                        extraDetails = extraDetails,
                        onExtraDetailsChange = { extraDetails = it
                            onExtraDetailsChange(it)
                        },
                        onSubmit = {
                            onReport()
                            navController.popBackStack(
                                "reason",
                                inclusive = false
                            )
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ReasonScreen(onNext: (String) -> Unit, onDismiss: () -> Unit) {
    val reasons = LocalContext.current.resources.getStringArray(R.array.report_reasons)
    var selectedReason by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(R.string.reason_step),
            style = MaterialTheme.typography.titleMedium
        )

        reasons.forEach { reason ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (selectedReason == reason),
                    onClick = { selectedReason = reason }
                )
                Text(
                    reason,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.close), color = MaterialTheme.colorScheme.onError)
            }

            Button(
                onClick = { if (selectedReason.isNotEmpty()) onNext(selectedReason) },
                enabled = selectedReason.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.next), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun DetailsScreen(reason: String, onNext: (String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val detailsOptions = when (reason) {
        "Spam" -> context.resources.getStringArray(R.array.details_options_spam)
        "Inappropriate Content" -> context.resources.getStringArray(R.array.details_options_inappropriate_content)
        "Harassment" -> context.resources.getStringArray(R.array.details_options_harassment)
        "Other" -> context.resources.getStringArray(R.array.details_options_other)
        else -> emptyArray()
    }

    var selectedDetail by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(R.string.details_step),
            style = MaterialTheme.typography.titleMedium
        )

        detailsOptions.forEach { detail ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { selectedDetail = detail }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (selectedDetail == detail),
                    onClick = { selectedDetail = detail }
                )
                Text(
                    detail,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(stringResource(R.string.back), color = MaterialTheme.colorScheme.onSecondary)
            }

            Button(
                onClick = { if (selectedDetail.isNotEmpty()) onNext(selectedDetail) },
                enabled = selectedDetail.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.next), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun SendScreen(
    extraDetails: String,
    onExtraDetailsChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(stringResource(R.string.send_step), style = MaterialTheme.typography.titleMedium)

        TextField(
            value = extraDetails,
            onValueChange = onExtraDetailsChange,
            label = { Text(stringResource(R.string.extra_details_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(stringResource(R.string.back), color = MaterialTheme.colorScheme.onSecondary)
            }

            Button(onClick = {
                onSubmit()
            }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text(stringResource(R.string.send), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun ReportProgressBar(currentStep: Int) {
    val colors = List(3) { MaterialTheme.colorScheme.onBackground }.toMutableList()
    if (currentStep in colors.indices) colors[currentStep] = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.report_title), style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            colors.forEachIndexed { index, color ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .background(color)
                    )

                    Text(
                        when (index) {
                            0 -> "Razón"
                            1 -> "Detalles"
                            2 -> "Enviar"
                            else -> ""
                        },
                        color = if (currentStep == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }

                if (index < colors.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

fun getCurrentStep(route: String?): Int {
    return when (route) {
        "reason" -> 0
        "details/{reason}" -> 1
        "send" -> 2
        else -> 0
    }
}