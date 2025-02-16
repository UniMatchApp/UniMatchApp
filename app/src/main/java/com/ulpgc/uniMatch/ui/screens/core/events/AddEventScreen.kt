package com.ulpgc.uniMatch.ui.screens.core.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.mocks.EventsMocks.createMockSurvey
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.ui.components.event.EventSection

@Composable
fun AddEventScreen(
    eventViewModel: EventViewModel
) {
    val survey = createMockSurvey()

    Column(modifier = Modifier.padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
            Text("Image Placeholder")
        }
        Spacer(modifier = Modifier.height(8.dp))
        EventSection(
            label = stringResource(R.string.event_title),
            value = "Enter a title",
            readOnly = false,
            onValueChange = { /* TODO */ }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventSection(
            label = stringResource(R.string.event_date),
            value = "mm-dd-yyyyThh:mm:ssZ",
            readOnly = false,
            onValueChange = { /* TODO */ }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventSection(
            label = stringResource(R.string.event_location),
            value = "Select a location",
            readOnly = false,
            onValueChange = { /* TODO */ }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = survey.title, style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                survey.options.forEach { (question, answers) ->
                    Text(question, style = MaterialTheme.typography.bodyMedium)
                    Column {
                        answers.forEach { option ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = false, onClick = { /* TODO */ })
                                Text(option)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.event_add_survey),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.event_complete),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}