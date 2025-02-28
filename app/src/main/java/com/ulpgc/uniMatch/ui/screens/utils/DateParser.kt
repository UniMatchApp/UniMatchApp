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

        fun formatStringToDate(date: String): Date {
            val possiblePatterns = listOf("dd/MM/yyyy HH:mm", "d/M/yyyy HH:mm", "dd-M-yyyy HH:mm")
            for (pattern in possiblePatterns) {
                try {
                    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
                    return formatter.parse(date)!!
                } catch (e: Exception) {

                }
            }
            throw IllegalArgumentException("Formato de fecha no válido")
        }

    }
}
