package com.masum.devfolio.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun formatFriendlyDateTime(isoString: String?): String {
    if (isoString.isNullOrBlank()) return ""

    return runCatching {
        val instant = Instant.parse(isoString)
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val monthName = when (local.monthNumber) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> ""
        }

        val hour12 = if (local.hour == 0) 12 else if (local.hour > 12) local.hour - 12 else local.hour
        val amPm = if (local.hour >= 12) "PM" else "AM"
        val minuteStr = local.minute.toString().padStart(2, '0')

        "$monthName ${local.dayOfMonth}, ${local.year} · $hour12:$minuteStr $amPm"
    }.getOrElse { isoString }
}
