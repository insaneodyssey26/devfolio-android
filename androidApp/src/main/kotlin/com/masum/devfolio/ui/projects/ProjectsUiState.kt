package com.masum.devfolio.ui.projects

import com.masum.devfolio.model.Project

sealed interface ProjectsUiState {
    data object Loading : ProjectsUiState
    data class Success(
        val projects: List<Project>,
        val isRefreshing: Boolean = false
    ) : ProjectsUiState
    data class Error(val message: String) : ProjectsUiState
}
