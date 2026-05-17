package com.sample.feature.albumlist.impl

import com.sample.core.data.model.Album

sealed interface AlbumListUiState {
    data class Success(val albums: List<Album>) : AlbumListUiState
    data object Loading : AlbumListUiState
    data class Error(val message: String) : AlbumListUiState
}