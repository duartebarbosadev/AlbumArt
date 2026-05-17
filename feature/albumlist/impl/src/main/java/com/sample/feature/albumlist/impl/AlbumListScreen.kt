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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.core.data.model.Album
import com.sample.core.ui.DevicePreviews

@Composable
fun AlbumListScreen(
    albums: List<Album>, // TODO Use viewmodel to get the data
    onAlbumClick : (albumId : String) -> Unit
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
    AlbumListScreen(
        albums = listOf(
            Album(id = "1", title = "Album 1", artist = "Artist 1", imageURL = null),
            Album(id = "2", title = "Album 2", artist = "Artist 2", imageURL = null),
            Album(id = "3", title = "Album 3", artist = "Artist 3", imageURL = null),
        ),

        onAlbumClick = {}
    )
}