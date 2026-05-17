package com.sample.feature.albumlist.impl

import com.sample.core.data.model.Album

sealed interface AlbumUiState {
    data class Success(val albums: List<Album>) : AlbumUiState
    data object Loading : AlbumUiState
    data class Error(val message: String) : AlbumUiState
}