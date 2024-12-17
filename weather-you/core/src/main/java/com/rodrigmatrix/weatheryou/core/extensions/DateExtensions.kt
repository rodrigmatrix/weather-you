package com.rodrigmatrix.weatheryou.core.extensions

import android.content.Context
import android.text.format.DateFormat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat

fun String.getTimeZoneHourAndMinutes(context: Context): String {
    return try {
        val localTime = LocalTime(DateTimeZone.forID(this))
        val pattern = if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh:mm aa"
        }
        localTime.toString(pattern)
    } catch (e: Exception) {
        ""
    }
}

fun DateTime.getLocalTime(): LocalTime {
    return try {
        return this.toLocalTime()
    } catch (e: Exception) {
        LocalTime()
    }
}

fun String.getDateTimeFromTimezone(): DateTime {
    return try {
        DateTime(DateTimeZone.forID(this))
    } catch (e: Exception) {
        DateTime()
    }
}

fun DateTime.getHoursAndMinutesDiff(second: DateTime): Pair<Int, Int> {
    val timeLeft = Period(
        this.getLocalTime(),
        second.getLocalTime(),
    )
    return Pair(timeLeft.hours, timeLeft.minutes)
}

fun DateTime.getHourWithMinutesString(context: Context): String {
    return try {
        val localTime = this.toLocalTime()
        val pattern = if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh:mm aa"
        }
        localTime.toString(pattern)
    } catch (e: Exception) {
        ""
    }
}

fun DateTime.getHourString(context: Context): String {
    return try {
        val localTime = this.toLocalTime()
        val pattern = if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh aa"
        }
        localTime.toString(pattern)
    } catch (e: Exception) {
        ""
    }
}

fun DateTime.getDateWithMonth(): String {
    return try {
        val pattern = DateTimeFormat.forPattern("EEEE, MMM dd")
        pattern.print(this).replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        ""
    }
}

fun DateTime.getFullDate(): String {
    return try {
        val pattern = DateTimeFormat.forPattern("EEEE, dd MMMM, YYYY")
        pattern.print(this).replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        ""
    }
}

fun DateTime.getDayString(): String {
    return try {
        val pattern = DateTimeFormat.forPattern("EEE")
        pattern.print(this).replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        ""
    }
}

fun DateTime.getFullDayString(): String {
    return try {
        val pattern = DateTimeFormat.forPattern("EEEE")
        pattern.print(this).replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        ""
    }
}

fun DateTime.getDayNumberString(): String {
    return try {
        this.dayOfMonth.toString()
    } catch (e: Exception) {
        ""
    }
}

