package com.masum.devfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.masum.devfolio.theme.PillShape
import com.masum.devfolio.theme.rememberDevfolioHaptics
import com.masum.devfolio.util.launchDevfolioCustomTab

@Composable
fun PoweredByDevfolioBadge(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptics = rememberDevfolioHaptics()

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, PillShape)
            .clickable {
                haptics.lightTick()
                launchDevfolioCustomTab(context, "https://devfolio.co")
            }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color(0xFF3770FF), CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Powered by Devfolio",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
