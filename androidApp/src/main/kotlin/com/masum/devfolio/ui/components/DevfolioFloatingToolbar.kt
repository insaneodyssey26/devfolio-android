package com.masum.devfolio.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masum.devfolio.theme.rememberDevfolioHaptics

enum class HackathonFilterTab(val title: String, val apiType: String) {
    OPEN("Open", "application_open"),
    UPCOMING("Upcoming", "upcoming"),
    ENDED("Ended", "ended")
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DevfolioFloatingToolbar(
    selectedTab: HackathonFilterTab,
    onTabSelected: (HackathonFilterTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberDevfolioHaptics()

    HorizontalFloatingToolbar(
        modifier = modifier.padding(bottom = 16.dp),
        expanded = true
    ) {
        HackathonFilterTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            FilterChip(
                selected = isSelected,
                onClick = {
                    haptics.lightTick()
                    onTabSelected(tab)
                },
                label = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
