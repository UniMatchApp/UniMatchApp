package com.ulpgc.uniMatch.ui.components.event

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper.Companion.openLocationInMap

@Composable
fun EventSection(
    label: String,
    value: String,
    readOnly: Boolean = true,
    isLocation: Boolean = false,
    placeholder: String = "",
    onValueChange: ((String) -> Unit)? = null
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { if (!readOnly) onValueChange?.invoke(it) },
            enabled = !readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = isLocation) {
                    if (isLocation) {
                        openLocationInMap(context, value)
                    }
                },
            placeholder = { Text(placeholder) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            readOnly = readOnly
        )
    }
}
