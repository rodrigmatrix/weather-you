package com.rodrigmatrix.weatheryou.presentation.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

fun String.getCurrentTime(): String {
    return try {
        val zone = DateTimeZone.forID(this)
        val dateTime = DateTime(zone)
        val output = dateTime.toLocalTime().toDateTimeToday().toDate()
        SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(output)
    } catch (e: Exception) {
        this
    }
}

fun String.getHourString(): String {
    return try {
        val localTime = LocalTime(this)
        val output = localTime.toDateTimeToday().toDate()
        SimpleDateFormat("hhaa", Locale.getDefault()).format(output)
    } catch (e: Exception) {
        this
    }
}

fun String.getDateWithMonth(): String {
    return try {
        val date = DateTime(this).toDate()
        SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        this
    }
}