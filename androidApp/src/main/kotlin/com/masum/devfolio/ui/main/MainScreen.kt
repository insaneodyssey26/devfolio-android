package com.masum.devfolio.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.ui.components.DevfolioBottomBar
import com.masum.devfolio.ui.components.MainNavigationTab
import com.masum.devfolio.ui.discover.DiscoverScreen
import com.masum.devfolio.ui.projects.ProjectsScreen
import com.masum.devfolio.ui.saved.SavedScreen

@Composable
fun MainScreen(
    onHackathonClick: (Hackathon) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainNavigationTab.DISCOVER) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            DevfolioBottomBar(
                currentTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainNavigationTab.DISCOVER -> {
                    DiscoverScreen(
                        onHackathonClick = onHackathonClick
                    )
                }

                MainNavigationTab.PROJECTS -> {
                    ProjectsScreen()
                }

                MainNavigationTab.SAVED -> {
                    SavedScreen(
                        onHackathonClick = onHackathonClick
                    )
                }
            }
        }
    }
}
