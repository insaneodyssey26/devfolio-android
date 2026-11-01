package com.masum.devfolio.util

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlinx.datetime.TimeZone

class DateFormatterTest {

    @Test
    fun testNullOrEmptyDateReturnsEmptyString() {
        assertEquals("", formatFriendlyDateTime(null))
        assertEquals("", formatFriendlyDateTime(""))
        assertEquals("", formatFriendlyDateTime("   "))
    }

    @Test
    fun testValidIsoDateFormatting() {
        val iso = "2026-11-20T14:30:00Z"
        val formatted = formatFriendlyDateTime(iso, TimeZone.UTC)
        assertEquals("Nov 20, 2026 · 2:30 PM", formatted)
    }

    @Test
    fun testInvalidIsoDateFallsBackGracefully() {
        val invalid = "not-a-valid-date"
        assertEquals("not-a-valid-date", formatFriendlyDateTime(invalid))
    }
}
