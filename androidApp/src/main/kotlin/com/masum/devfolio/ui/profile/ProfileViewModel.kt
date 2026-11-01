package com.masum.devfolio.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.masum.devfolio.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _handleInput = MutableStateFlow("")
    val handleInput: StateFlow<String> = _handleInput.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = profileRepository.getSavedUsername()
            if (!saved.isNullOrBlank()) {
                _handleInput.value = saved
                fetchProfile(saved)
            }
        }
    }

    fun onHandleChange(newHandle: String) {
        _handleInput.value = newHandle
    }

    fun refreshProfile() {
        val handle = _handleInput.value.trim().removePrefix("@")
        if (handle.isNotBlank()) {
            fetchProfile(handle)
        }
    }

    fun onSignedIn(username: String) {
        _handleInput.value = username
        fetchProfile(username)
    }

    fun linkProfile() {
        val handle = _handleInput.value.trim().removePrefix("@")
        if (handle.isNotBlank()) {
            fetchProfile(handle)
        }
    }

    fun unlinkProfile() {
        viewModelScope.launch {
            profileRepository.clearUsername()
            _handleInput.value = ""
            _uiState.value = ProfileUiState.Idle
        }
    }

    private fun fetchProfile(username: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            profileRepository.getProfileByUsername(username)
                .onSuccess { profile ->
                    profileRepository.saveUsername(profile.username)
                    _uiState.value = ProfileUiState.Success(profile)
                }
                .onFailure { throwable ->
                    _uiState.value = ProfileUiState.Error(
                        throwable.message ?: "Could not load Devfolio profile for @$username"
                    )
                }
        }
    }

    companion object {
        fun provideFactory(profileRepository: ProfileRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProfileViewModel(profileRepository) as T
                }
            }
    }
}
