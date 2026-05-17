package com.sample.core.data.repository

import com.sample.core.data.model.Album
import com.sample.core.data.model.toListAlbums
import com.sample.core.network.NetworkRequests
import jakarta.inject.Inject

// TODO Different Impl like testImpl with hardcoded data to avoid using web always in dev environment
class AlbumsRepositoryImpl @Inject constructor(
    private val networkRequests: NetworkRequests,
) : AlbumsRepository {

    override suspend fun getAlbums() : List<Album> {
        return networkRequests.getItunesRss().toListAlbums()
    }
}