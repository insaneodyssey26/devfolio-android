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
    private var currentFrom = 0
    private var isEndReached = false
    private var isLoadingMore = false

    init {
        loadHackathons(currentTab)
    }

    fun selectTab(tab: HackathonFilterTab) {
        if (tab == currentTab && _uiState.value !is DiscoverUiState.Error) return
        currentTab = tab
        currentFrom = 0
        isEndReached = false
        loadHackathons(tab)
    }

    fun refresh() {
        val current = _uiState.value
        if (current is DiscoverUiState.Success) {
            _uiState.value = current.copy(isRefreshing = true)
        }
        currentFrom = 0
        isEndReached = false
        viewModelScope.launch {
            repository.getHackathonsStream(currentTab.apiType, from = 0, size = 20).collect { result ->
                result.onSuccess { hackathons ->
                    currentFrom = hackathons.size
                    isEndReached = hackathons.size < 20
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

    fun loadMore() {
        val current = _uiState.value
        if (current !is DiscoverUiState.Success || isLoadingMore || isEndReached || current.isRefreshing) return

        isLoadingMore = true
        viewModelScope.launch {
            repository.getHackathonsStream(currentTab.apiType, from = currentFrom, size = 20).collect { result ->
                result.onSuccess { newHackathons ->
                    if (newHackathons.isEmpty()) {
                        isEndReached = true
                    } else {
                        val existingSlugs = current.hackathons.map { it.slug }.toSet()
                        val uniqueNew = newHackathons.filter { it.slug !in existingSlugs }
                        currentFrom += newHackathons.size
                        isEndReached = newHackathons.size < 20
                        _uiState.value = current.copy(hackathons = current.hackathons + uniqueNew)
                    }
                }
                isLoadingMore = false
            }
        }
    }

    fun retry() {
        currentFrom = 0
        isEndReached = false
        loadHackathons(currentTab)
    }

    private fun loadHackathons(tab: HackathonFilterTab) {
        _uiState.value = DiscoverUiState.Loading
        currentFrom = 0
        isEndReached = false
        viewModelScope.launch {
            repository.getHackathonsStream(tab.apiType, from = 0, size = 20).collect { result ->
                result.onSuccess { hackathons ->
                    currentFrom = hackathons.size
                    isEndReached = hackathons.size < 20
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
