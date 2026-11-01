package com.masum.devfolio.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masum.devfolio.repository.DevfolioRepository
import com.masum.devfolio.repository.DevfolioRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val repository: DevfolioRepository = DevfolioRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProjectsUiState>(ProjectsUiState.Loading)
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    private var currentFrom = 0
    private var isEndReached = false
    private var isLoadingMore = false

    init {
        loadProjects()
    }

    fun refresh() {
        val current = _uiState.value
        if (current is ProjectsUiState.Success) {
            _uiState.value = current.copy(isRefreshing = true)
        }
        currentFrom = 0
        isEndReached = false
        viewModelScope.launch {
            repository.getProjectsStream(from = 0, size = 25).collect { result ->
                result.onSuccess { projects ->
                    currentFrom = projects.size
                    isEndReached = projects.size < 25
                    _uiState.value = ProjectsUiState.Success(
                        projects = projects,
                        isRefreshing = false
                    )
                }.onFailure { error ->
                    _uiState.value = ProjectsUiState.Error(
                        error.message ?: "Failed to refresh projects"
                    )
                }
            }
        }
    }

    fun loadMore() {
        val current = _uiState.value
        if (current !is ProjectsUiState.Success || isLoadingMore || isEndReached || current.isRefreshing) return

        isLoadingMore = true
        viewModelScope.launch {
            repository.getProjectsStream(from = currentFrom, size = 25).collect { result ->
                result.onSuccess { newProjects ->
                    if (newProjects.isEmpty()) {
                        isEndReached = true
                    } else {
                        val existingSlugs = current.projects.map { it.slug }.toSet()
                        val uniqueNew = newProjects.filter { it.slug !in existingSlugs }
                        currentFrom += newProjects.size
                        isEndReached = newProjects.size < 25
                        _uiState.value = current.copy(projects = current.projects + uniqueNew)
                    }
                }
                isLoadingMore = false
            }
        }
    }

    fun retry() {
        currentFrom = 0
        isEndReached = false
        loadProjects()
    }

    private fun loadProjects() {
        _uiState.value = ProjectsUiState.Loading
        currentFrom = 0
        isEndReached = false
        viewModelScope.launch {
            repository.getProjectsStream(from = 0, size = 25).collect { result ->
                result.onSuccess { projects ->
                    currentFrom = projects.size
                    isEndReached = projects.size < 25
                    _uiState.value = ProjectsUiState.Success(
                        projects = projects,
                        isRefreshing = false
                    )
                }.onFailure { error ->
                    _uiState.value = ProjectsUiState.Error(
                        error.message ?: "Failed to load projects"
                    )
                }
            }
        }
    }
}
