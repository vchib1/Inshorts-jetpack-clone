package dev.vivekchib.inshortsapp.core.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseISO8601(iso8601: String): LocalDateTime {
    return LocalDateTime.parse(iso8601, DateTimeFormatter.ISO_DATE_TIME)
}

fun LocalDateTime.formattedDateTime(): String? {
    return try {
        DateTimeFormatter.ofPattern("dd MMM, yyyy hh:mm a").format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}