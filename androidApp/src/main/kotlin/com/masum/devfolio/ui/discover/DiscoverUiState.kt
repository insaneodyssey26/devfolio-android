package com.masum.devfolio.ui.discover

import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.ui.components.HackathonFilterTab

sealed interface DiscoverUiState {
    data object Loading : DiscoverUiState
    data class Success(
        val hackathons: List<Hackathon>,
        val selectedTab: HackathonFilterTab,
        val isRefreshing: Boolean = false
    ) : DiscoverUiState
    data class Error(val message: String) : DiscoverUiState
}
