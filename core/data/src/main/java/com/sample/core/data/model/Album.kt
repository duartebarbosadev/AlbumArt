package com.sample.core.data.model

import com.sample.core.network.model.ItunesRssResponseDto
import java.net.URL

data class Album(
    val id: String?,
    val title: String?,
    val name: String?,
    val artist: String?,
    val imageURL: URL?,
    val largeImageURL: URL?,
    val itemCount: String?,
    val price: String?,
    val currency: String?,
    val contentType: String?,
    val rights: String?,
    val releaseDate: String?,
    val category: String?,
    val albumUrl: URL?,
)



fun ItunesRssResponseDto.toListAlbums(): List<Album> =
    feed.entry.map { entry ->
        Album(
            id = entry.id?.attributes?.imId ?: entry.id?.label,
            title = entry.title?.label,
            name = entry.name?.label,
            artist = entry.artist?.label,
            imageURL = entry.images.firstOrNull()?.label?.toUrlOrNull(),
            largeImageURL = entry.images.lastOrNull()?.label?.toUrlOrNull(),
            itemCount = entry.itemCount?.label,
            price = entry.price?.label,
            currency = entry.price?.attributes?.currency,
            contentType = entry.contentType?.attributes?.label
                ?: entry.contentType?.innerContentType?.attributes?.label,
            rights = entry.rights?.label,
            releaseDate = entry.releaseDate?.attributes?.label
                ?: entry.releaseDate?.label,
            category = entry.category?.attributes?.label,
            albumUrl = entry.link?.attributes?.href?.toUrlOrNull(),
        )
    }

private fun String.toUrlOrNull(): URL? =
    runCatching { URL(this) }.getOrNull()