package com.sample.feature.albumlist.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sample.core.data.model.Album
import com.sample.core.ui.DevicePreviews

@Composable
fun AlbumListScreen(
    onAlbumClick : (albumId : String) -> Unit,
    viewModel: AlbumListViewModel = hiltViewModel()
) {

    val albumDataState by viewModel.uiState.collectAsState()

    when (albumDataState) {
        is AlbumUiState.Loading -> {
            Box(modifier = Modifier) {
                Text("Loading...")
            }
        }
        is AlbumUiState.Error -> {
            Box(modifier = Modifier) {
                Text("Error: ${(albumDataState as AlbumUiState.Error).message}")
            }
        }
        is AlbumUiState.Success -> {
            AlbumList(albums = (albumDataState as AlbumUiState.Success).albums, onAlbumClick)
        }
    }
}

@Composable
private fun AlbumList(
    albums: List<Album>,
    onAlbumClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(albums.size) { index ->
            val album = albums[index]
            AlbumItem(
                album = album,
                onClick = { album.id?.let { onAlbumClick(it) } }
            )
        }
    }
}


@Composable
fun AlbumItem(
    album: Album,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = album.title?: "Unknown Album")
        }
    }
}

@DevicePreviews
@Composable
fun AlbumListScreenPreview() {
    AlbumList(
        onAlbumClick = {},
        albums = listOf(
            Album(id = "1", title = "Album 1", artist = "Artist 1", imageURL = null),
            Album(id = "2", title = "Album 2", artist = "Artist 2", imageURL = null),
            Album(id = "3", title = "Album 3", artist = "Artist 3", imageURL = null),
            Album(id = "4", title = "Album 4", artist = "Artist 4", imageURL = null),
            Album(id = "5", title = "Album 5", artist = "Artist 5", imageURL = null),
            Album(id = "6", title = "Album 6", artist = "Artist 6", imageURL = null)
        ),
    )
}