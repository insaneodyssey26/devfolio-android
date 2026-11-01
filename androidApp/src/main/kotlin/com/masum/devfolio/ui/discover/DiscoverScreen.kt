package com.masum.devfolio.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.theme.rememberDevfolioHaptics
import com.masum.devfolio.ui.components.DevfolioFloatingToolbar
import com.masum.devfolio.ui.components.FullScreenLoader
import com.masum.devfolio.ui.components.HackathonFilterTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onHackathonClick: (Hackathon) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "DEVFOLIO",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is DiscoverUiState.Loading -> {
                    FullScreenLoader()
                }

                is DiscoverUiState.Error -> {
                    DiscoverErrorState(
                        message = state.message,
                        onRetry = viewModel::retry
                    )
                }

                is DiscoverUiState.Success -> {
                    DiscoverSuccessContent(
                        state = state,
                        onRefresh = viewModel::refresh,
                        onHackathonClick = onHackathonClick
                    )

                    DevfolioFloatingToolbar(
                        selectedTab = state.selectedTab,
                        onTabSelected = viewModel::selectTab,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverSuccessContent(
    state: DiscoverUiState.Success,
    onRefresh: () -> Unit,
    onHackathonClick: (Hackathon) -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        state = pullRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        if (state.hackathons.isEmpty()) {
            DiscoverEmptyState(selectedTab = state.selectedTab)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = state.hackathons,
                    key = { it.uuid }
                ) { hackathon ->
                    HackathonCard(
                        hackathon = hackathon,
                        onClick = { onHackathonClick(hackathon) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverEmptyState(
    selectedTab: HackathonFilterTab,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No ${selectedTab.title.lowercase()} hackathons found",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DiscoverErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberDevfolioHaptics()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                haptics.heavyClick()
                onRetry()
            }
        ) {
            Text(text = "Try Again")
        }
    }
}
