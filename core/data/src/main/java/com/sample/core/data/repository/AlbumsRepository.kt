package com.sample.core.data.repository

import com.sample.core.data.model.Album

// TODO Interface with possibility of choosing test data etc
interface AlbumsRepository {
    suspend fun getAlbums() : List<Album>
    suspend fun getAlbumById(albumId: String) : Album?
}