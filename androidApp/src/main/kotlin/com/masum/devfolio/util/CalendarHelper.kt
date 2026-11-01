package com.masum.devfolio.util

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.masum.devfolio.model.Hackathon
import kotlin.time.Instant

fun addHackathonToCalendar(context: Context, hackathon: Hackathon) {
    val deadlineIso = hackathon.regEndsAt ?: hackathon.endsAt ?: return
    val deadlineMillis = runCatching { Instant.parse(deadlineIso).toEpochMilliseconds() }.getOrNull() ?: return

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, "${hackathon.name} - Registration Deadline")
        putExtra(CalendarContract.Events.DESCRIPTION, "${hackathon.tagline}\nApply at: https://${hackathon.slug}.devfolio.co")
        putExtra(CalendarContract.Events.EVENT_LOCATION, hackathon.location ?: (if (hackathon.isOnline) "Online" else "In-Person"))
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, deadlineMillis - (3600 * 1000L))
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, deadlineMillis)
    }

    context.startActivity(intent)
}
