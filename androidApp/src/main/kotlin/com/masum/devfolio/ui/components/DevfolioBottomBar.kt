package com.masum.devfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masum.devfolio.theme.rememberDevfolioHaptics

enum class MainNavigationTab {
    DISCOVER, PROJECTS
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DevfolioBottomBar(
    currentTab: MainNavigationTab,
    onTabSelected: (MainNavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberDevfolioHaptics()

    HorizontalFloatingToolbar(
        modifier = modifier.padding(bottom = 16.dp),
        expanded = true
    ) {
        NavigationBarItem(
            selected = currentTab == MainNavigationTab.DISCOVER,
            onClick = {
                haptics.lightTick()
                onTabSelected(MainNavigationTab.DISCOVER)
            },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Discover") },
            label = { Text("Discover", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentTab == MainNavigationTab.PROJECTS,
            onClick = {
                haptics.lightTick()
                onTabSelected(MainNavigationTab.PROJECTS)
            },
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Projects") },
            label = { Text("Showcase", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )
    }
}
