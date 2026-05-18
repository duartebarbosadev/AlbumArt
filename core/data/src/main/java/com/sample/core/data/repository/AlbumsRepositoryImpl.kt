package com.sample.core.data.repository

import com.sample.core.data.model.Album
import com.sample.core.data.model.toListAlbums
import com.sample.core.network.NetworkRequests
import jakarta.inject.Inject

// TODO Different Impl like testImpl with hardcoded data to avoid using web always in dev environment
class AlbumsRepositoryImpl
    @Inject
    constructor(
        private val networkRequests: NetworkRequests,
    ) : AlbumsRepository {
        private val albumsCache = mutableListOf<Album>()

        override suspend fun getAlbums(): List<Album> {
            albumsCache.clear()
            albumsCache.addAll(networkRequests.getItunesRss().toListAlbums())
            return albumsCache
        }

        override suspend fun getAlbumById(albumId: String): Album? {
            // If the album exists in cache, return it, if not, fetch the albums and then try to return the requested album
            if (albumsCache.any { it.id == albumId }) {
                return albumsCache.first { it.id == albumId }
            } else {
                getAlbums()
            }
            return albumsCache.firstOrNull { it.id == albumId }
        }
    }
