package com.ulpgc.uniMatch.ui.screens.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateParser() {

    companion object {
        fun formatDateToString(date: Date, pattern: String = "dd-MM-yyyy HH:mm"): String {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            return formatter.format(date)
        }

        fun formatStringToDate(date: String, pattern: String = "dd-MM-yyyy HH:mm"): Date {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            return formatter.parse(date)
        }
    }
}
