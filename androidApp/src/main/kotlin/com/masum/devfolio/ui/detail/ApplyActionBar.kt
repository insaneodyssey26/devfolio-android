package com.masum.devfolio.ui.detail

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.theme.rememberDevfolioHaptics
import com.masum.devfolio.util.addHackathonToCalendar
import com.masum.devfolio.util.launchDevfolioCustomTab

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ApplyActionBar(
    hackathon: Hackathon,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptics = rememberDevfolioHaptics()
    var isMenuExpanded by remember { mutableStateOf(false) }

    val applyUrl = "https://${hackathon.slug}.devfolio.co"

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            SplitButtonLayout(
                leadingButton = {
                    SplitButtonDefaults.LeadingButton(
                        onClick = {
                            haptics.heavyClick()
                            launchDevfolioCustomTab(context, applyUrl, accentColor)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text(
                            text = "Apply with Devfolio",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                },
                trailingButton = {
                    SplitButtonDefaults.TrailingButton(
                        checked = isMenuExpanded,
                        onCheckedChange = {
                            haptics.lightTick()
                            isMenuExpanded = it
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Options",
                            modifier = Modifier.size(SplitButtonDefaults.TrailingIconSize)
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Add to Calendar") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                    },
                    onClick = {
                        haptics.lightTick()
                        isMenuExpanded = false
                        addHackathonToCalendar(context, hackathon)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Share Hackathon") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                    },
                    onClick = {
                        haptics.lightTick()
                        isMenuExpanded = false
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, hackathon.name)
                            putExtra(Intent.EXTRA_TEXT, "Check out ${hackathon.name} on Devfolio: $applyUrl")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Hackathon"))
                    }
                )
            }
        }
    }
}
