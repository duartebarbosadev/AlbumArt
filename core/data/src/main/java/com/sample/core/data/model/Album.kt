package com.sample.core.data.model

import com.sample.core.network.model.ItunesRssResponseDto
import java.net.URL

data class Album(
    val id : String?,
    val title : String?,
    val artist : String?,
    val imageURL: URL?
)

fun ItunesRssResponseDto.toListAlbums() =
    feed.entry.map { entry ->
        Album(
            id = entry.id?.label,
            title = entry.title?.label,
            artist = entry.artist?.label,
            imageURL = entry.images.firstOrNull()?.label?.let { URL(it) }
        )
    }