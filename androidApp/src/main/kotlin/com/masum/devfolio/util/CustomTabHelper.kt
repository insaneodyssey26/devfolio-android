package com.masum.devfolio.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun launchDevfolioCustomTab(
    context: Context,
    url: String,
    toolbarColor: Color = Color(0xFF3770FF)
) {
    val intent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(toolbarColor.toArgb())
                .build()
        )
        .setShowTitle(true)
        .build()

    intent.launchUrl(context, Uri.parse(url))
}
