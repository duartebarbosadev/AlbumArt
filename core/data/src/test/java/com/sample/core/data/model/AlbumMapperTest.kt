package com.sample.core.data.model

import com.sample.core.network.model.ArtistDto
import com.sample.core.network.model.EntryDto
import com.sample.core.network.model.FeedDto
import com.sample.core.network.model.IdAttributesDto
import com.sample.core.network.model.IdDto
import com.sample.core.network.model.ImageDto
import com.sample.core.network.model.ItunesRssResponseDto
import com.sample.core.network.model.LabelDto
import com.sample.core.network.model.LinkAttributesDto
import com.sample.core.network.model.LinkDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlbumMapperTest {

    @Test
    fun `maps iTunes response to albums`() {
        val expectedId = "1"
        val expectedName = "Album 1"
        val expectedTitle = "Album 1 - Artist 1"
        val expectedArtist = "Artist 1"
        val expectedSmallImageUrl = "https://example.com/album1-small.jpg"
        val expectedLargeImageUrl = "https://example.com/album1-large.jpg"
        val expectedAlbumUrl = "https://example.com/album1"

        val dto = ItunesRssResponseDto(
            feed = FeedDto(
                entry = listOf(
                    EntryDto(
                        name = LabelDto(expectedName),
                        title = LabelDto(expectedTitle),
                        artist = ArtistDto(label = expectedArtist),
                        id = IdDto(attributes = IdAttributesDto(imId = expectedId)),
                        images = listOf(
                            ImageDto(label = expectedSmallImageUrl),
                            ImageDto(label = expectedLargeImageUrl),
                        ),
                        link = LinkDto(
                            attributes = LinkAttributesDto(
                                href = expectedAlbumUrl,
                            ),
                        ),
                    ),
                ),
            ),
        )

        val result = dto.toListAlbums()

        assertEquals(1, result.size)
        assertEquals(expectedId, result[0].id)
        assertEquals(expectedName, result[0].name)
        assertEquals(expectedTitle, result[0].title)
        assertEquals(expectedArtist, result[0].artist)
        assertEquals(expectedSmallImageUrl, result[0].imageURL)
        assertEquals(expectedLargeImageUrl, result[0].largeImageURL)
        assertEquals(expectedAlbumUrl, result[0].albumUrl.toString())
    }

    @Test
    fun `maps empty feed to empty album list`() {
        val dto = ItunesRssResponseDto(feed = FeedDto(entry = emptyList()))

        val result = dto.toListAlbums()

        assertTrue(result.isEmpty())
    }
}
