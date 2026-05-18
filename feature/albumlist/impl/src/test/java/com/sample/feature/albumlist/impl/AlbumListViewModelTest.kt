package com.sample.feature.albumlist.impl

import com.sample.core.data.model.Album
import com.sample.core.data.repository.AlbumsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AlbumListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<AlbumsRepository>()

    @Test
    fun `loads albums successfully`() =
        runTest {
            val albums = listOf(testAlbum(id = "1"))
            coEvery { repository.getAlbums() } returns albums

            val viewModel = AlbumListViewModel(repository)

            assertEquals(AlbumListUiState.Success(albums), viewModel.uiState.value)
            coVerify(exactly = 1) { repository.getAlbums() }
        }

    @Test
    fun `shows error when loading albums fails`() =
        runTest {
            coEvery { repository.getAlbums() } throws RuntimeException("Network failed")

            val viewModel = AlbumListViewModel(repository)

            assertEquals(
                AlbumListUiState.Error("Network failed"),
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
