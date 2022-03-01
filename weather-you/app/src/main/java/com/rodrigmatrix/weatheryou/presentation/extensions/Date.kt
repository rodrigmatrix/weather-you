package com.rodrigmatrix.weatheryou.presentation.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

fun String.getTimeZoneCurrentTime(): LocalTime {
    return try {
        val zone = DateTimeZone.forID(this)
        return LocalTime(zone)
    } catch (e: Exception) {
        LocalTime()
    }
}

fun Long.getLocalTime(): LocalTime {
    return try {
        return LocalTime(this)
    } catch (e: Exception) {
        LocalTime()
    }
}

fun String.getLocalTime(): LocalTime {
    return try {
        return LocalTime(this)
    } catch (e: Exception) {
        LocalTime()
    }
}

fun LocalTime.getHoursAndMinutesDiff(second: LocalTime): Pair<Int, Int> {
    val timeLeft = Period(this, second)
    return Pair(timeLeft.hours, timeLeft.minutes)
}

fun DateTime.getTimeString(): String {
    return try {
        val localTime = LocalTime(this)
        val output = localTime.toDateTimeToday().toDate()
        SimpleDateFormat("hh:mm", Locale.getDefault()).format(output)
    } catch (e: Exception) {
        ""
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