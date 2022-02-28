package com.rodrigmatrix.weatheryou.presentation.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import java.text.SimpleDateFormat
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

fun String.getCurrentHour(timeZone: String): Int {
    return try {
        val zone = DateTimeZone.forID(timeZone)
        DateTime(this, zone).hourOfDay
    } catch (e: Exception) {
        0
    }
}

fun String.getHourString(): String {
    return try {
        val localTime = LocalTime(this)
        val output = localTime.toDateTimeToday().toDate()
        SimpleDateFormat("hh:mm", Locale.getDefault()).format(output)
    } catch (e: Exception) {
        this
    }
}

fun String.getHourWithMinutesString(): String {
    return try {
        val localTime = LocalTime(this)
        val output = localTime.toDateTimeToday().toDate()
        SimpleDateFormat("hh:mmaa", Locale.getDefault()).format(output)
    } catch (e: Exception) {
        this
    }
}

fun String.getDateWithMonth(): String {
    return try {
        val date = DateTime(this).toDate()
        SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        this
    }
}