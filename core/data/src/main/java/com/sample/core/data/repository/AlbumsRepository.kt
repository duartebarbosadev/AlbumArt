package com.sample.core.data.repository

import com.sample.core.data.model.Album
import com.sample.core.data.model.toListAlbums
import com.sample.core.network.RssClient

class AlbumsRepository(
    private val rssClient: RssClient, // TODO Inject
) {

    fun getAlbums() : List<Album> {
        return rssClient.getItunesRss().toListAlbums()
    }
}