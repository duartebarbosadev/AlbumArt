package com.sample.feature.albumlist.impl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.data.repository.AlbumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AlbumListViewModel
    @Inject
    constructor(
        private val repository: AlbumsRepository,
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)
        val uiState: StateFlow<AlbumListUiState> = _uiState.asStateFlow()

        init {
            loadAlbums()
        }

        private fun loadAlbums() {
            viewModelScope.launch {
                runCatching {
                    repository.getAlbums()
                }.onSuccess { albums ->
                    _uiState.value = AlbumListUiState.Success(albums)
                }.onFailure { error ->
                    _uiState.value =
                        AlbumListUiState.Error(
                            error.message?.takeIf { it.isNotBlank() }
                                ?: context.getString(R.string.error_loading_albums_message),
                        )
                }
            }
        }
    }
