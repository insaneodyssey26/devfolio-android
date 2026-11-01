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

    init {
        loadProjects()
    }

    fun refresh() {
        val current = _uiState.value
        if (current is ProjectsUiState.Success) {
            _uiState.value = current.copy(isRefreshing = true)
        }
        viewModelScope.launch {
            repository.getProjectsStream().collect { result ->
                result.onSuccess { projects ->
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

    fun retry() {
        loadProjects()
    }

    private fun loadProjects() {
        _uiState.value = ProjectsUiState.Loading
        viewModelScope.launch {
            repository.getProjectsStream().collect { result ->
                result.onSuccess { projects ->
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
