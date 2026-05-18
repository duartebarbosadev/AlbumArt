package com.sample.core.data.repository

import com.sample.core.data.model.Album

/**
 * Repository interface for fetching album data.
 */
interface AlbumsRepository {
    suspend fun getAlbums(): List<Album>

    suspend fun getAlbumById(albumId: String): Album?
}
