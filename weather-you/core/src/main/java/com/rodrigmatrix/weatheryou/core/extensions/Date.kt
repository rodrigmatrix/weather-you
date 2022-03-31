package com.rodrigmatrix.weatheryou.core.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import org.joda.time.Period
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun String.getTimeZoneCurrentTime(): LocalTime {
    return try {
        val zone = DateTimeZone.forID(this)
        return LocalTime(zone)
    } catch (e: Exception) {
        LocalTime()
    }
}

fun String.getTimeZoneTimestamp(): Long {
    return try {
        return DateTime(this).millis
    } catch (e: Exception) {
        0
    }
}

fun Long.getLocalTime(): LocalTime {
    return try {
        return DateTime(this.toUnixTimestamp()).toLocalTime()
    } catch (e: Exception) {
        LocalTime()
    }
}

fun Long.getDateTime(): DateTime {
    return try {
        return DateTime(this.toUnixTimestamp())
    } catch (e: Exception) {
        DateTime()
    }
}

fun Long.getHoursAndMinutesDiff(second: Long): Pair<Int, Int> {
    val timeLeft = Period(
        this.getLocalTime(),
        second.getLocalTime()
    )
    return Pair(timeLeft.hours, timeLeft.minutes)
}

fun Long.toStringPattern(pattern: String): String {
    return try {
        val dateTime = Date(this.toUnixTimestamp())
        SimpleDateFormat(pattern, Locale.getDefault()).format(dateTime)
    } catch (e: Exception) {
        ""
    }
}

fun Long.getHourWithMinutesString(): String {
    return try {
        val localTime = LocalTime(this.toUnixTimestamp())
        val output = localTime.toDateTimeToday().toDate()
        SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(output)
    } catch (e: Exception) {
        ""
    }
}

fun Long.getHourString(): String {
    return try {
        val localTime = LocalTime(this.toUnixTimestamp())
        val output = localTime.toDateTimeToday().toDate()
        SimpleDateFormat("hh aa", Locale.getDefault()).format(output)
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