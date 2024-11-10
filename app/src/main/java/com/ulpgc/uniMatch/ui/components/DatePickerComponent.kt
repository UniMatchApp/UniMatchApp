package com.ulpgc.uniMatch.ui.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ulpgc.uniMatch.R
import java.util.Calendar

@Composable
fun DatePickerComponent(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val displayedDate = remember { mutableStateOf(selectedDate) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val newDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            displayedDate.value = newDate
            onDateSelected(newDate)
        },
        year, month, day
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.selected_date) + ": ${displayedDate.value}")

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { datePickerDialog.show() }
        ) {
            Text(text = stringResource(R.string.select_date))
        }
    }
}
