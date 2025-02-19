package com.ulpgc.uniMatch.ui.screens.core.events

import LocationPicker
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.ui.components.event.EventDatePicker
import com.ulpgc.uniMatch.ui.components.event.EventSection


@Composable
fun AddEventScreen(
    eventViewModel: EventViewModel
) {

    Column(modifier = Modifier
        .padding(16.dp)
        ,
    ) {
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
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = stringResource(R.string.event_location),
                color = MaterialTheme.colorScheme.onBackground
            )
            LocationPicker(
                onChangeLocation = { eventLocation ->
                    Log.i("AddEventScreen", "Location updated: $eventLocation")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = stringResource(R.string.event_date),
                color = MaterialTheme.colorScheme.onBackground
            )
            EventDatePicker()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
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



