package com.masum.devfolio.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.repository.BookmarkRepository
import com.masum.devfolio.repository.BookmarkRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SavedUiState {
    data class Content(val bookmarks: List<Hackathon>) : SavedUiState
}

class SavedViewModel(
    private val bookmarkRepository: BookmarkRepository = BookmarkRepositoryImpl
) : ViewModel() {

    val bookmarks: StateFlow<List<Hackathon>> = bookmarkRepository
        .getBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeBookmark(hackathon: Hackathon) {
        viewModelScope.launch {
            bookmarkRepository.toggleBookmark(hackathon)
        }
    }
}
