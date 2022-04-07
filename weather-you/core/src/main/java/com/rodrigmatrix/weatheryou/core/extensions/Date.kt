package com.rodrigmatrix.weatheryou.core.extensions

import android.content.Context
import android.text.format.DateFormat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import org.joda.time.Period
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun String.getTimeZoneHourAndMinutes(context: Context): String {
    return try {
        val calendar = DateTime(DateTimeZone.forID(this))
        val pattern = if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh:mm aa"
        }
        SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.toDate())
    } catch (e: Exception) {
        ""
    }
}

fun Long.getLocalTime(): LocalTime {
    return try {
        return DateTime(this.toUnixTimestamp()).toLocalTime()
    } catch (e: Exception) {
        LocalTime()
    }
}

fun Long.getHoursAndMinutesDiff(second: Long): Pair<Int, Int> {
    val timeLeft = Period(
        this.getLocalTime(),
        second.getLocalTime()
    )
    return Pair(timeLeft.hours, timeLeft.minutes)
}

fun Long.getHourWithMinutesString(context: Context): String {
    return try {
        val localTime = LocalTime(this.toUnixTimestamp())
        val output = localTime.toDateTimeToday().toDate()
        val pattern = if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh:mm aa"
        }
        SimpleDateFormat(pattern, Locale.getDefault()).format(output)
    } catch (e: Exception) {
        ""
    }
}

fun Long.getHourString(context: Context): String {
    return try {
        val localTime = LocalTime(this.toUnixTimestamp())
        val output = localTime.toDateTimeToday().toDate()
        val pattern = if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh aa"
        }
        SimpleDateFormat(pattern, Locale.getDefault()).format(output)
    } catch (e: Exception) {
        ""
    }
}

fun Long.getDateWithMonth(): String {
    return try {
        val date = DateTime(this.toUnixTimestamp()).toDate()
        SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        ""
    }
}

private fun Long.toUnixTimestamp(): Long = this * 1000L