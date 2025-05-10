package com.ulpgc.uniMatch.ui.components.event.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R

@Composable
fun SurveyBottomButtons(
    onAddOption: () -> Unit,
    onConfirmSurvey: () -> Unit,
    optionsSize: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (optionsSize < 10) {
            Button(onClick = {
                onAddOption()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_option))
                Text(
                    stringResource(R.string.add_option),
                    color = colorScheme.onPrimary

                )
            }

        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = {
            onConfirmSurvey()
        }) {
            Text(
                stringResource(R.string.create_survey),
                color = colorScheme.onPrimary
            )
        }
    }
}