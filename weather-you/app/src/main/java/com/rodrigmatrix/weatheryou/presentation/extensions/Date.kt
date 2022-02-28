package com.rodrigmatrix.weatheryou.presentation.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
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

fun String.getDateTime(): DateTime {
    return try {
        return DateTime(this)
    } catch (e: Exception) {
        DateTime()
    }
}

fun String.getLocalTime(): LocalTime {
    return try {
        return LocalTime(this)
    } catch (e: Exception) {
        LocalTime()
    }
}

fun LocalTime.getDayLengthHours(sunset: LocalTime): String {
    return try {
        val timeLeft = this.minusMillis(sunset.millisOfDay)

        return if (timeLeft.millisOfDay != 0) {
            timeLeft.toString("hh:mm")
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}

fun LocalTime.getRemainingDaylightHours(sunset: LocalTime): String {
    return try {
        return this.minusMillis(sunset.millisOfDay).toString("hh:mm")
    } catch (e: Exception) {
        ""
    }
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