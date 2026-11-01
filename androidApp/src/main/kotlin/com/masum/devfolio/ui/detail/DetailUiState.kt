package com.masum.devfolio.ui.detail

import com.masum.devfolio.model.Hackathon

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val hackathon: Hackathon) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
