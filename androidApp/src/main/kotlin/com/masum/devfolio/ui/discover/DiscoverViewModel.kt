package com.masum.devfolio.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masum.devfolio.repository.DevfolioRepository
import com.masum.devfolio.repository.DevfolioRepositoryImpl
import com.masum.devfolio.ui.components.HackathonFilterTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val repository: DevfolioRepository = DevfolioRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private var currentTab = HackathonFilterTab.OPEN

    init {
        loadHackathons(currentTab)
    }

    fun selectTab(tab: HackathonFilterTab) {
        if (tab == currentTab && _uiState.value !is DiscoverUiState.Error) return
        currentTab = tab
        loadHackathons(tab)
    }

    fun refresh() {
        val current = _uiState.value
        if (current is DiscoverUiState.Success) {
            _uiState.value = current.copy(isRefreshing = true)
        }
        viewModelScope.launch {
            repository.getHackathonsStream(currentTab.apiType).collect { result ->
                result.onSuccess { hackathons ->
                    _uiState.value = DiscoverUiState.Success(
                        hackathons = hackathons,
                        selectedTab = currentTab,
                        isRefreshing = false
                    )
                }.onFailure { error ->
                    _uiState.value = DiscoverUiState.Error(
                        message = error.message ?: "Failed to refresh hackathons"
                    )
                }
            }
        }
    }

    fun retry() {
        loadHackathons(currentTab)
    }

    private fun loadHackathons(tab: HackathonFilterTab) {
        _uiState.value = DiscoverUiState.Loading
        viewModelScope.launch {
            repository.getHackathonsStream(tab.apiType).collect { result ->
                result.onSuccess { hackathons ->
                    _uiState.value = DiscoverUiState.Success(
                        hackathons = hackathons,
                        selectedTab = tab,
                        isRefreshing = false
                    )
                }.onFailure { error ->
                    _uiState.value = DiscoverUiState.Error(
                        message = error.message ?: "Failed to load hackathons"
                    )
                }
            }
        }
    }
}
