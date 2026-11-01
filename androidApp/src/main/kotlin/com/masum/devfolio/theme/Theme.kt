package com.masum.devfolio.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DevfolioBlue,
    onPrimary = DarkTextPrimary,
    primaryContainer = DevfolioBlueDark,
    onPrimaryContainer = DarkTextPrimary,
    secondary = DevfolioCyan,
    onSecondary = DarkTextPrimary,
    background = DarkSurface,
    onBackground = DarkTextPrimary,
    surface = DarkSurfaceElevated,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceContainer,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkSurfaceBorder
)

private val LightColorScheme = lightColorScheme(
    primary = DevfolioBlue,
    onPrimary = LightSurfaceElevated,
    primaryContainer = DevfolioCyan,
    onPrimaryContainer = LightSurfaceElevated,
    secondary = DevfolioBlueDark,
    onSecondary = LightSurfaceElevated,
    background = LightSurface,
    onBackground = LightTextPrimary,
    surface = LightSurfaceElevated,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceContainer,
    onSurfaceVariant = LightTextSecondary,
    outline = LightSurfaceBorder
)

@Composable
fun DevfolioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DevfolioTypography,
        shapes = DevfolioShapes,
        content = content
    )
}
