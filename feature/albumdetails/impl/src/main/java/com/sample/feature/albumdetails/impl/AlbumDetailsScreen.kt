package com.sample.feature.albumdetails.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sample.core.data.model.Album
import com.sample.core.ui.DevicePreviews

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumDetailsScreen(
    viewModel: AlbumDetailsViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val uiState = uiState) {
        is AlbumDetailsUiState.Loading -> {
            LoadingState()
        }

        is AlbumDetailsUiState.Success -> {
            AlbumDetail(
                album = uiState.album,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }

        is AlbumDetailsUiState.Error -> {
            ErrorState(message = uiState.message)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumDetail(
    album: Album,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    AlbumDetailContent(
        album = album,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumDetailContent(
    album: Album,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val albumArtworkModel = album.largeImageURL ?: album.imageURL

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

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
    ) {
        AsyncImage(
            model = albumArtworkModel,
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxSize()
                    .scale(1.18f)
                    .blur(54.dp)
                    .alpha(0.58f),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(
                        colors =
                            listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.18f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.34f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
                            ),
                    )),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = albumArtworkModel,
                contentDescription = album.name ?: album.title ?: "Album cover",
                modifier =
                    coverModifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = album.name ?: album.title ?: "Unknown Album",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = titleModifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = album.artist ?: "Unknown Artist",
                modifier = artistModifier,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            val category = album.category
            if (!category.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(18.dp))
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Text(
                        text = category.uppercase(),
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.58f),
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AlbumStat(label = "Tracks", value = album.itemCount)
                    AlbumStat(label = "Price", value = album.price)
                    AlbumStat(label = "Type", value = album.contentType)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                Text(
                    text = "Album info",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                AlbumDetailRow(
                    label = "Category",
                    value = album.category,
                )

                AlbumDetailRow(
                    label = "Release date",
                    value = album.releaseDate,
                )

                AlbumDetailRow(
                    label = "Tracks",
                    value = album.itemCount,
                )

                AlbumDetailRow(
                    label = "Price",
                    value = album.price,
                )

                AlbumDetailRow(
                    label = "Type",
                    value = album.contentType,
                )

                AlbumDetailRow(
                    label = "Copyright",
                    value = album.rights,
                )
            }
        }
    }
}

@Composable
private fun AlbumDetailRow(
    label: String,
    value: String?,
) {
    if (value.isNullOrBlank()) return

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = label,
                modifier = Modifier.width(112.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = value,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun AlbumStat(
    label: String,
    value: String?,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.takeUnless { it.isNullOrBlank() } ?: "-",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
        Text(
            text = "Loading...",
            modifier = Modifier.padding(top = 14.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
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
                    text = "Could not load album",
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
}

@DevicePreviews
@Composable
fun AlbumDetailsScreenPreview() {
    AlbumDetail(
        album =
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
    )
}

@DevicePreviews
@Composable
fun LoadingStatePreview() {
    LoadingState()
}

@DevicePreviews
@Composable
fun ErrorStatePreview() {
    ErrorState(message = "An error occurred while fetching the album details. Please try again later.")
}
