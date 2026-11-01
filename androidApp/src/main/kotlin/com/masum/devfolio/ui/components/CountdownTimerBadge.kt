package com.masum.devfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masum.devfolio.theme.PillShape
import com.masum.devfolio.theme.StatusUrgent
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun CountdownTimerBadge(
    deadlineIso: String?,
    modifier: Modifier = Modifier
) {
    if (deadlineIso.isNullOrBlank()) return

    var remainingText by remember(deadlineIso) { mutableStateOf("") }
    var isUrgent by remember(deadlineIso) { mutableStateOf(false) }

    LaunchedEffect(deadlineIso) {
        val targetInstant = runCatching { Instant.parse(deadlineIso) }.getOrNull() ?: return@LaunchedEffect

        while (true) {
            val now = Clock.System.now()
            val diffSeconds = (targetInstant - now).inWholeSeconds

            if (diffSeconds <= 0L) {
                remainingText = "Registration closed"
                isUrgent = false
                break
            }

            val days = diffSeconds / 86400L
            val hours = (diffSeconds % 86400L) / 3600L
            val minutes = (diffSeconds % 3600L) / 60L

            isUrgent = days < 2L

            remainingText = when {
                days > 0L -> "Closes in ${days}d ${hours}h"
                hours > 0L -> "Closes in ${hours}h ${minutes}m"
                else -> "Closes in ${minutes}m"
            }

            delay(60_000)
        }
    }

    if (remainingText.isNotEmpty()) {
        val textColor = if (isUrgent) StatusUrgent else MaterialTheme.colorScheme.onSurfaceVariant
        val bgColor = if (isUrgent) StatusUrgent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant

        Box(
            modifier = modifier
                .background(bgColor, PillShape)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = remainingText,
                style = MaterialTheme.typography.labelSmall,
                color = textColor
            )
        }
    }
}
