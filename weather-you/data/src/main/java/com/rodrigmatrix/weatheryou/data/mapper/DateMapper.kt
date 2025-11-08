package com.rodrigmatrix.weatheryou.data.mapper

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

private const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

fun DateTime.toEntityString(): String {
    return this.toString(DATE_PATTERN)
}

fun String.toDateTime(timezone: String): DateTime {
    return DateTime.parse(
        this, DateTimeFormat.forPattern(DATE_PATTERN)
    ).toDateTime(DateTimeZone.forID(timezone.ifEmpty { java.util.TimeZone.getDefault().id }))
}

fun String.toDateTime(): DateTime {
    return try {
        DateTime.parse(this, DateTimeFormat.forPattern(DATE_PATTERN))
    } catch (_: Exception) {
        try {
            DateTimeFormat.forPattern(DATE_PATTERN).parseLocalDateTime(this).toDateTime()
        } catch (_: Exception) {
            DateTime.now()
        }
    }
}

fun String?.getCurrentTime(): DateTime {
    return DateTime.now(DateTimeZone.forID(this))
}
