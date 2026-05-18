package com.sample.feature.albumdetails.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.data.repository.AlbumsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AlbumDetailsViewModel.Factory::class)
class AlbumDetailsViewModel
    @AssistedInject
    constructor(
        private val repository: AlbumsRepository,
        @Assisted val albumId: String,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<AlbumDetailsUiState>(AlbumDetailsUiState.Loading)
        val uiState = _uiState.asStateFlow()

        init {
            loadAlbum()
        }

        private fun loadAlbum() {
            viewModelScope.launch {
                runCatching {
                    _uiState.value = AlbumDetailsUiState.Loading
                    repository.getAlbumById(albumId)
                }.onSuccess { album ->
                    if (album == null) {
                        _uiState.value = AlbumDetailsUiState.Error("We could not find that album.")
                    } else {
                        _uiState.value = AlbumDetailsUiState.Success(album)
                    }
                }.onFailure { error ->
                    _uiState.value =
                        AlbumDetailsUiState.Error(
                            error.message?.takeIf { it.isNotBlank() }
                                ?: "Could not load album details. Check your connection and try again.",
                        )
                }
            }
        }

        @AssistedFactory
        interface Factory {
            fun create(albumId: String): AlbumDetailsViewModel
        }
    }
