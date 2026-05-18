package com.sample.core.data.repository

import com.sample.core.network.NetworkRequests
import com.sample.core.network.model.ArtistDto
import com.sample.core.network.model.EntryDto
import com.sample.core.network.model.FeedDto
import com.sample.core.network.model.IdAttributesDto
import com.sample.core.network.model.IdDto
import com.sample.core.network.model.ImageDto
import com.sample.core.network.model.ItunesRssResponseDto
import com.sample.core.network.model.LabelDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

/**
 * Album Repository Implentation Test
 */
class AlbumRepositoryImplTest {

    private val networkRequests = mockk<NetworkRequests>()

    @Test
    fun `getAlbums returns mapped albums from network`() = runTest {
        coEvery { networkRequests.getItunesRss() } returns rssResponse()

        val repository = AlbumsRepositoryImpl(networkRequests)

        val result = repository.getAlbums()

        Assert.assertEquals(2, result.size)
        Assert.assertEquals("1", result[0].id)
        Assert.assertEquals("Album 1", result[0].name)
        Assert.assertEquals("Artist 1", result[0].artist)
        coVerify(exactly = 1) { networkRequests.getItunesRss() }
    }

    @Test
    fun `getAlbumById fetches albums when cache is empty`() = runTest {
        coEvery { networkRequests.getItunesRss() } returns rssResponse()

        val repository = AlbumsRepositoryImpl(networkRequests)

        val result = repository.getAlbumById("2")

        Assert.assertEquals("2", result?.id)
        Assert.assertEquals("Album 2", result?.name)
        coVerify(exactly = 1) { networkRequests.getItunesRss() }
    }

    @Test
    fun `getAlbumById uses cache when album is already loaded`() = runTest {
        coEvery { networkRequests.getItunesRss() } returns rssResponse()

        val repository = AlbumsRepositoryImpl(networkRequests)

        repository.getAlbums()
        val result = repository.getAlbumById("1")

        Assert.assertEquals("1", result?.id)

        // Make sure getItunesRss was only called once
        coVerify(exactly = 1) { networkRequests.getItunesRss() }
    }

    @Test
    fun `getAlbumById returns null when album does not exist`() = runTest {
        coEvery { networkRequests.getItunesRss() } returns rssResponse()

        val repository = AlbumsRepositoryImpl(networkRequests)

        val result = repository.getAlbumById("missing")

        Assert.assertNull(result)
    }

    private fun rssResponse() = ItunesRssResponseDto(
        feed = FeedDto(
            entry = listOf(
                albumEntry(id = "1", name = "Album 1", artist = "Artist 1"),
                albumEntry(id = "2", name = "Album 2", artist = "Artist 2"),
            ),
        ),
    )

    private fun albumEntry(id: String, name: String, artist: String) = EntryDto(
        name = LabelDto(name),
        title = LabelDto("$name - $artist"),
        artist = ArtistDto(label = artist),
        id = IdDto(attributes = IdAttributesDto(imId = id)),
        images = listOf(
            ImageDto(label = "https://example.com/$id-small.jpg"),
            ImageDto(label = "https://example.com/$id-large.jpg"),
        ),
    )
}