package com.earl.gpns.ui.core

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CurrentDateAndTimeGiver {

    private val currentDate = Date()
    val standardFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    fun fetchCurrentDateAndTime(): LocalDateTime {
        val simpleDateFormat: DateFormat =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val dateText = simpleDateFormat.format(currentDate)
        return LocalDateTime.parse(dateText, standardFormatter)
    }

    fun fetchCurrentDateAsString(): String {
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    fun fetchDayOfYearFormat(): DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    fun fetchDayOfMonthFormat() : DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM")

    fun fetchTimeOfDayFormat() : DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun initDateTime(timestamp: String) : String {
        var time = ""
        val currentDateText = fetchCurrentDateAndTime()
        val lastAuthDate = LocalDateTime.parse(timestamp, standardFormatter)
        val dayOfMonthFormatter = fetchDayOfMonthFormat()
        val timeOfDayFormatter = fetchTimeOfDayFormat()
        val dayOfYearFormatter = fetchDayOfYearFormat()
        time = if (lastAuthDate.format(dayOfMonthFormatter) == currentDateText.format(dayOfMonthFormatter)) {
            lastAuthDate.format(timeOfDayFormatter)
        } else if (lastAuthDate.format(dayOfYearFormatter) == currentDateText.format(dayOfYearFormatter)) {
            lastAuthDate.format(dayOfMonthFormatter)
        } else {
            lastAuthDate.format(dayOfYearFormatter)
        }
        return time
    }
}