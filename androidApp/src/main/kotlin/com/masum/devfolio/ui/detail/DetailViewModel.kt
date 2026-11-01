package com.masum.devfolio.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.repository.BookmarkRepository
import com.masum.devfolio.repository.BookmarkRepositoryImpl
import com.masum.devfolio.repository.DevfolioRepository
import com.masum.devfolio.repository.DevfolioRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val slug: String,
    private val repository: DevfolioRepository = DevfolioRepositoryImpl(),
    private val bookmarkRepository: BookmarkRepository = BookmarkRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    val isBookmarked: StateFlow<Boolean> = bookmarkRepository
        .isBookmarked(slug)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        loadDetail()
    }

    fun toggleBookmark(hackathon: Hackathon) {
        viewModelScope.launch {
            bookmarkRepository.toggleBookmark(hackathon)
        }
    }

    fun retry() {
        loadDetail()
    }

    private fun loadDetail() {
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            repository.getHackathon(slug)
                .onSuccess { hackathon ->
                    _uiState.value = DetailUiState.Success(hackathon)
                }
                .onFailure { error ->
                    _uiState.value = DetailUiState.Error(
                        error.message ?: "Failed to load hackathon details"
                    )
                }
        }
    }
}
