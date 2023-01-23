package com.earl.gpns.ui

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

    fun fetchDayOfYearFormat(): DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    fun fetchDayOfMonthFormat() : DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM")

    fun fetchTimeOfDayFormat() : DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
}