package com.sample.feature.albumdetails.impl

import com.sample.core.data.model.Album

sealed interface AlbumDetailsUiState {
    data class Success(
        val album: Album,
    ) : AlbumDetailsUiState

    data object Loading : AlbumDetailsUiState

    data class Error(
        val message: String,
    ) : AlbumDetailsUiState
}
