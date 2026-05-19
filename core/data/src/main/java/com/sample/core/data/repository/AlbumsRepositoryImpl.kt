package com.sample.core.data.repository

import com.sample.core.data.model.Album
import com.sample.core.data.model.toListAlbums
import com.sample.core.network.NetworkRequests
import jakarta.inject.Inject
import javax.inject.Singleton

// TODO Different Impl like testImpl with hardcoded data to avoid using web always in dev environment

/**
 * A data repository implementation for [AlbumsRepository] that fetches album data from the network
 *  and caches it in memory to avoid future requests.
 */
@Singleton
class AlbumsRepositoryImpl
    @Inject
    constructor(
        private val networkRequests: NetworkRequests,
    ) : AlbumsRepository {
        private var albumsCache: List<Album> = emptyList()

        override suspend fun getAlbums(): List<Album> {
            albumsCache = networkRequests.getItunesRss().toListAlbums()
            return albumsCache
        }

        override suspend fun getAlbumById(albumId: String): Album? {
            // If there's no album with that ID cached, get the albums first
            if (albumsCache.none { it.id == albumId }) {
                getAlbums()
            }
            return albumsCache.firstOrNull { it.id == albumId }
        }
    }
