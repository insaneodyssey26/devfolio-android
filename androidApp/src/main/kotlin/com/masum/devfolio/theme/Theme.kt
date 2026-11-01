package com.masum.devfolio.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun createDarkScheme(
    primary: Color,
    primaryContainer: Color,
    pureBlack: Boolean
) = darkColorScheme(
    primary = primary,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = primaryContainer,
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = primary.copy(alpha = 0.8f),
    onSecondary = Color(0xFFFFFFFF),
    background = if (pureBlack) PureBlackBackground else DarkSurface,
    onBackground = DarkTextPrimary,
    surface = if (pureBlack) PureBlackSurface else DarkSurfaceElevated,
    onSurface = DarkTextPrimary,
    surfaceVariant = if (pureBlack) PureBlackContainer else DarkSurfaceContainer,
    onSurfaceVariant = DarkTextSecondary,
    surfaceContainer = if (pureBlack) PureBlackContainer else DarkSurfaceContainer,
    surfaceContainerHigh = if (pureBlack) Color(0xFF1C1C1C) else Color(0xFF262F3D),
    surfaceContainerHighest = if (pureBlack) Color(0xFF262626) else Color(0xFF303B4C),
    outline = if (pureBlack) PureBlackBorder else DarkSurfaceBorder,
    outlineVariant = if (pureBlack) Color(0xFF1E1E1E) else Color(0xFF242C38)
)

private fun createLightScheme(
    primary: Color,
    primaryContainer: Color
) = lightColorScheme(
    primary = primary,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = primaryContainer,
    onPrimaryContainer = primary,
    secondary = primary.copy(alpha = 0.85f),
    onSecondary = Color(0xFFFFFFFF),
    background = LightSurface,
    onBackground = LightTextPrimary,
    surface = LightSurfaceElevated,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceContainer,
    onSurfaceVariant = LightTextSecondary,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    outline = LightSurfaceBorder,
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun DevfolioTheme(
    themeMode: String = "system",
    colorTheme: String = "devfolio",
    pureBlack: Boolean = false,
    fontPreference: String = "google_sans",
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val context = LocalContext.current
    val view = LocalView.current
    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme = when {
        colorTheme == "system" && supportsDynamic -> {
            if (isDark) {
                val dynamic = dynamicDarkColorScheme(context)
                if (pureBlack) {
                    dynamic.copy(
                        background = PureBlackBackground,
                        surface = PureBlackSurface,
                        surfaceContainer = PureBlackContainer,
                        surfaceContainerHigh = Color(0xFF1C1C1C),
                        surfaceContainerHighest = Color(0xFF262626)
                    )
                } else dynamic
            } else {
                dynamicLightColorScheme(context)
            }
        }
        else -> {
            val (lightPrimary, darkPrimary, darkContainer, lightContainer) = when (colorTheme) {
                "amber" -> listOf(AmberPrimaryLight, AmberPrimary, AmberContainerDark, AmberContainerLight)
                "ember" -> listOf(EmberPrimaryLight, EmberPrimary, EmberContainerDark, EmberContainerLight)
                "emerald" -> listOf(EmeraldPrimaryLight, EmeraldPrimary, EmeraldContainerDark, EmeraldContainerLight)
                "ocean" -> listOf(OceanPrimaryLight, OceanPrimary, OceanContainerDark, OceanContainerLight)
                "orchid" -> listOf(OrchidPrimaryLight, OrchidPrimary, OrchidContainerDark, OrchidContainerLight)
                else -> listOf(DevfolioBlue, DevfolioBlue, DevfolioBlueDark, DevfolioBlueContainerLight)
            }

            if (isDark) {
                createDarkScheme(darkPrimary, darkContainer, pureBlack)
            } else {
                createLightScheme(lightPrimary, lightContainer)
            }
        }
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    val typography = androidx.compose.runtime.remember(fontPreference) {
        getDevfolioTypography(fontPreference)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = DevfolioShapes,
        content = content
    )
}
