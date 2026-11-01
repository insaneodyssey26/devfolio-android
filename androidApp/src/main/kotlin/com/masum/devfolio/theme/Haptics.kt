package com.masum.devfolio.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

class DevfolioHaptics(private val haptic: HapticFeedback) {
    fun lightTick() {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    fun heavyClick() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
}

@Composable
fun rememberDevfolioHaptics(): DevfolioHaptics {
    val haptic = LocalHapticFeedback.current
    return remember(haptic) { DevfolioHaptics(haptic) }
}
