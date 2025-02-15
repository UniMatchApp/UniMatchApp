package com.ulpgc.uniMatch.ui.screens.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatesParser(context: Context) {

    init {
        Companion.context = context.applicationContext
    }

    companion object {

        private lateinit var context: Context

        fun formatDateToString(date: Date, pattern: String = "dd-MM-yyyy HH:mm"): String {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            return formatter.format(date)
        }
    }
}