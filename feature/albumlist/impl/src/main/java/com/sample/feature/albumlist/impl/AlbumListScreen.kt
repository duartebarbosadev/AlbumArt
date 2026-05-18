package com.sample.feature.albumlist.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sample.core.data.model.Album
import com.sample.core.ui.DevicePreviews

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumListScreen(
    onAlbumClick: (albumId: String) -> Unit,
    viewModel: AlbumListViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val albumDataState by viewModel.uiState.collectAsState()

    when (val albumDataState = albumDataState) {
        is AlbumListUiState.Loading -> {
            Box(modifier = Modifier) {
                Text("Loading...")
            }
        }

        is AlbumListUiState.Error -> {
            Box(modifier = Modifier) {
                Text("Error: ${albumDataState.message}")
            }
        }

        is AlbumListUiState.Success -> {
            AlbumList(
                albums = albumDataState.albums,
                onAlbumClick = onAlbumClick,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumList(
    albums: List<Album>,
    onAlbumClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(albums.size) { index ->
            val album = albums[index]
            AlbumItem(
                album = album,
                onClick = { album.id?.let(onAlbumClick) },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumItem(
    album: Album,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    var coverModifier =
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)

    var titleModifier: Modifier = Modifier

    if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            coverModifier =
                coverModifier.sharedElement(
                    sharedContentState =
                        rememberSharedContentState(
                            key = "album-cover-${album.id ?: album.title.orEmpty()}",
                        ),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            titleModifier =
                titleModifier.sharedElement(
                    sharedContentState =
                        rememberSharedContentState(
                            key = "album-title-${album.id ?: album.title.orEmpty()}",
                        ),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
        }
    }

    Card(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        Column {
            AsyncImage(
                model = album.largeImageURL ?: album.imageURL,
                contentDescription = album.name ?: album.title ?: "Album cover",
                modifier = coverModifier,
                contentScale = ContentScale.Crop,
            )

            Text(
                text = album.title ?: "Unknown Album",
                modifier = titleModifier,
            )
            // If album title doesn't contain the artist name, show the artist name as well
            if (album.title?.contains(album.artist ?: "") == false) {
                Text(text = album.artist ?: "Unknown Artist")
            }
        }
    }
}

@DevicePreviews
@Composable
fun AlbumListScreenPreview() {
    AlbumList(
        onAlbumClick = {},
        albums =
            listOf(
                Album(
                    id = "1440925843",
                    title = "Enema of the State - blink-182",
                    name = "Enema of the State",
                    artist = "blink-182",
                    imageURL = null,
                    largeImageURL = null,
                    itemCount = "12",
                    price = "$9.99",
                    currency = "USD",
                    contentType = "Album",
                    rights = "℗ 1999 UMG Recordings, Inc.",
                    releaseDate = "June 1, 1999",
                    category = "Alternative",
                    albumUrl = null,
                ),
                Album(
                    id = "1440839912",
                    title = "Take Off Your Pants and Jacket - blink-182",
                    name = "Take Off Your Pants and Jacket",
                    artist = "blink-182",
                    imageURL = null,
                    largeImageURL = null,
                    itemCount = "13",
                    price = "$9.99",
                    currency = "USD",
                    contentType = "Album",
                    rights = "℗ 2001 Geffen Records",
                    releaseDate = "June 12, 2001",
                    category = "Alternative",
                    albumUrl = null,
                ),
                Album(
                    id = "1440817613",
                    title = "blink-182 - blink-182",
                    name = "blink-182",
                    artist = "blink-182",
                    imageURL = null,
                    largeImageURL = null,
                    itemCount = "15",
                    price = "$9.99",
                    currency = "USD",
                    contentType = "Album",
                    rights = "℗ 2003 Geffen Records",
                    releaseDate = "November 18, 2003",
                    category = "Alternative",
                    albumUrl = null,
                ),
                Album(
                    id = "723478726",
                    title = "Neighborhoods - blink-182",
                    name = "Neighborhoods",
                    artist = "blink-182",
                    imageURL = null,
                    largeImageURL = null,
                    itemCount = "14",
                    price = "$9.99",
                    currency = "USD",
                    contentType = "Album",
                    rights = "℗ 2011 DGC Records",
                    releaseDate = "September 27, 2011",
                    category = "Alternative",
                    albumUrl = null,
                ),
                Album(
                    id = "1134376035",
                    title = "California - blink-182",
                    name = "California",
                    artist = "blink-182",
                    imageURL = null,
                    largeImageURL = null,
                    itemCount = "16",
                    price = "$9.99",
                    currency = "USD",
                    contentType = "Album",
                    rights = "℗ 2016 BMG Rights Management (US) LLC",
                    releaseDate = "July 1, 2016",
                    category = "Alternative",
                    albumUrl = null,
                ),
                Album(
                    id = "1705392720",
                    title = "ONE MORE TIME... - blink-182",
                    name = "ONE MORE TIME...",
                    artist = "blink-182",
                    imageURL = null,
                    largeImageURL = null,
                    itemCount = "17",
                    price = "$9.99",
                    currency = "USD",
                    contentType = "Album",
                    rights = "℗ 2023 Columbia Records",
                    releaseDate = "October 20, 2023",
                    category = "Alternative",
                    albumUrl = null,
                ),
            ),
    )
}
