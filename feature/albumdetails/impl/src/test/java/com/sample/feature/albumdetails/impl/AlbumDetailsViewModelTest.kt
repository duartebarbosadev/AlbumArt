package com.sample.feature.albumdetails.impl

import com.sample.core.data.model.Album
import com.sample.core.data.repository.AlbumsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AlbumDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<AlbumsRepository>()

    @Test
    fun `loads album successfully`() =
        runTest {
            val album = testAlbum(id = "1")
            coEvery { repository.getAlbumById("1") } returns album

            val viewModel = AlbumDetailsViewModel(repository, albumId = "1")

            assertEquals(AlbumDetailsUiState.Success(album), viewModel.uiState.value)
            coVerify(exactly = 1) { repository.getAlbumById("1") }
        }

    @Test
    fun `shows not found error when album is missing`() =
        runTest {
            coEvery { repository.getAlbumById("missing") } returns null

            val viewModel = AlbumDetailsViewModel(repository, albumId = "missing")

            assertEquals(
                AlbumDetailsUiState.Error("Album not found"),
                viewModel.uiState.value,
            )
        }

    @Test
    fun `shows error when repository fails`() =
        runTest {
            coEvery { repository.getAlbumById("1") } throws RuntimeException("Network failed")

            val viewModel = AlbumDetailsViewModel(repository, albumId = "1")

            assertEquals(
                AlbumDetailsUiState.Error("Network failed"),
                viewModel.uiState.value,
            )
        }

    private fun testAlbum(id: String) =
        Album(
            id = id,
            title = "Title $id",
            name = "Album $id",
            artist = "Artist $id",
            imageURL = null,
            largeImageURL = null,
            itemCount = null,
            price = null,
            currency = null,
            contentType = null,
            rights = null,
            releaseDate = null,
            category = null,
            albumUrl = null,
        )
}
