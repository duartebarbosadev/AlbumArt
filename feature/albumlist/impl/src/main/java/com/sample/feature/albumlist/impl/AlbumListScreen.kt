package com.sample.feature.albumlist.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sample.core.data.model.Album
import com.sample.core.ui.DevicePreviews

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumListScreen(
    onAlbumClick: (album: Album) -> Unit,
    viewModel: AlbumListViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val albumDataState by viewModel.uiState.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                MaterialTheme.colorScheme.surfaceContainerLow,
                                MaterialTheme.colorScheme.surface,
                            ),
                    ),
                ),
    ) {
        when (val albumDataState = albumDataState) {
            is AlbumListUiState.Loading -> {
                LoadingState()
            }

            is AlbumListUiState.Error -> {
                ErrorState(
                    message = albumDataState.message,
                    onRetry = viewModel::retry,
                )
            }

            is AlbumListUiState.Success -> {
                AlbumList(
                    albums = albumDataState.albums,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { query ->
                        searchQuery = query
                        viewModel.searchByAlbumName(query)
                    },
                    onAlbumClick = onAlbumClick,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumList(
    albums: List<Album>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAlbumClick: (Album) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AlbumSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 18.dp, end = 18.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 142.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            contentPadding = PaddingValues(start = 18.dp, top = 2.dp, end = 18.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            items(
                items = albums,
                key = { album -> album.id ?: album.title.orEmpty() },
            ) { album ->
                AlbumItem(
                    album = album,
                    onClick = { onAlbumClick(album) },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@Composable
private fun AlbumSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        leadingIcon = { SearchGlyph() },
        placeholder = {
            Text(
                text = stringResource(R.string.search_albums_hint),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
    )
}

@Composable
private fun SearchGlyph() {
    val color = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(modifier = Modifier.size(22.dp)) {
        val radius = size.minDimension * 0.28f
        val center = Offset(size.width * 0.42f, size.height * 0.42f)
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx()),
        )
        drawLine(
            color = color,
            start =
                Offset(
                    center.x + radius * 0.66f,
                    center.y + radius * 0.66f,
                ),
            end =
                Offset(
                    center.x + radius * 1.42f,
                    center.y + radius * 1.42f,
                ),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
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
    var artistModifier: Modifier = Modifier

    if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            coverModifier =
                coverModifier
                    .sharedElement(
                        sharedContentState =
                            rememberSharedContentState(
                                key = "album-cover-${album.id ?: album.title.orEmpty()}",
                            ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
            titleModifier =
                titleModifier
                    .sharedBounds(
                        sharedContentState =
                            rememberSharedContentState(
                                key = "album-title-${album.id ?: album.title.orEmpty()}",
                            ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    )
            artistModifier =
                artistModifier
                    .sharedBounds(
                        sharedContentState =
                            rememberSharedContentState(
                                key = "album-artist-${album.id ?: album.title.orEmpty()}",
                            ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    )
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = album.largeImageURL ?: album.imageURL,
            contentDescription = album.name ?: album.title ?: stringResource(com.sample.core.ui.R.string.album_cover_content_description),
            modifier =
                coverModifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = album.name ?: album.title ?: stringResource(com.sample.core.ui.R.string.unknown_album),
            modifier = titleModifier,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        val artist = album.artist
        if (!artist.isNullOrBlank()) {
            Text(
                text = artist,
                modifier = artistModifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        val category = album.category
        if (!category.isNullOrBlank()) {
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
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
            text = stringResource(R.string.loading_albums_list),
            modifier = Modifier.padding(top = 14.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.error_loading_albums_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = onRetry,
            ) {
                Text(text = stringResource(R.string.action_retry))
            }
        }
    }
}

@DevicePreviews
@Composable
fun AlbumListScreenPreview() {
    AlbumList(
        onAlbumClick = {},
        searchQuery = "",
        onSearchQueryChange = {},
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

@DevicePreviews
@Composable
fun AlbumListLoadingPreview() {
    LoadingState()
}
