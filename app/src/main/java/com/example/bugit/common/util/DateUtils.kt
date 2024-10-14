package com.example.bugit.common.util

import java.text.DateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    // Get the current date for creating the tab/sheet in Google sheet
     fun getCurrentDate(): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).formatDate()
    }

    private fun String.formatDate(): String {
        val parts = this.split("/")
        return if (parts.size == 3) {
            val (day, month, year) = parts
            "$day-$month-$year"
        } else {
            this // Return the original string if it doesn't match the expected format
        }
    }

}