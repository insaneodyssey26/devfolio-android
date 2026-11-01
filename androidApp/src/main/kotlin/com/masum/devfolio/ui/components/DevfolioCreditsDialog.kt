package com.masum.devfolio.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masum.devfolio.theme.rememberDevfolioHaptics
import com.masum.devfolio.util.launchDevfolioCustomTab

@Composable
fun DevfolioCreditsDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val haptics = rememberDevfolioHaptics()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Powered by Devfolio",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column {
                Text(
                    text = "This application is an open source community client built for Android developers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "All hackathon listings, project submissions, builder profiles, logos, and event schedules are owned and hosted by Devfolio (NSB Classic PTE LTD).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "We deeply respect Devfolio and its team for nurturing the developer ecosystem.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    haptics.lightTick()
                    onDismissRequest()
                    launchDevfolioCustomTab(context, "https://devfolio.co")
                }
            ) {
                Text("Visit Devfolio.co")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    haptics.lightTick()
                    onDismissRequest()
                }
            ) {
                Text("Close")
            }
        }
    )
}
