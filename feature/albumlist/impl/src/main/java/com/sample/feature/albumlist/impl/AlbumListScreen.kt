package com.sample.feature.albumlist.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sample.core.data.model.Album
import com.sample.core.ui.DevicePreviews

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumListScreen(
    onAlbumClick: (albumId: String) -> Unit,
    viewModel: AlbumListViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val albumDataState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Album Art",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Top iTunes albums",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                    MaterialTheme.colorScheme.surface,
                                ),
                        ),
                    ).padding(innerPadding),
        ) {
            when (val albumDataState = albumDataState) {
                is AlbumListUiState.Loading -> {
                    LoadingState()
                }

                is AlbumListUiState.Error -> {
                    ErrorState(message = albumDataState.message)
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
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
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

    ElevatedCard(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp),
        ) {
            AsyncImage(
                model = album.largeImageURL ?: album.imageURL,
                contentDescription = album.name ?: album.title ?: "Album cover",
                modifier =
                    coverModifier
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentScale = ContentScale.Crop,
            )

            Text(
                text = album.title ?: "Unknown Album",
                modifier =
                    titleModifier
                        .padding(horizontal = 12.dp),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            // If album title doesn't contain the artist name, show the artist name as well
            val artist = album.artist
            if (!artist.isNullOrBlank() && album.title?.contains(artist, ignoreCase = true) != true) {
                Text(
                    text = artist,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Text(
            text = "Finding fresh album art...",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ErrorState(message: String) {
    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        shape = RoundedCornerShape(28.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Could not load albums",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
            )
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
