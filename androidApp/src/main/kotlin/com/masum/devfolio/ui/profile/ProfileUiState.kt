package com.masum.devfolio.ui.profile

import com.masum.devfolio.data.model.DevfolioUserProfile

sealed interface ProfileUiState {
    data object Idle : ProfileUiState
    data object Loading : ProfileUiState
    data class Success(val profile: DevfolioUserProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}
