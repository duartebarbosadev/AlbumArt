package com.sample.feature.albumdetails.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumDetail(
    album: Album,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Album Details",
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    ),
            )
        },
    ) { innerPadding ->
        AlbumDetailContent(
            album = album,
            contentPadding = innerPadding,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumDetailContent(
    album: Album,
    contentPadding: PaddingValues,
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

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .breathingAlbumBackground()
                .padding(contentPadding),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors =
                    CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            ) {
                AsyncImage(
                    model = album.largeImageURL ?: album.imageURL,
                    contentDescription = album.name ?: album.title ?: "Album cover",
                    modifier =
                        coverModifier
                            .clip(RoundedCornerShape(32.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = album.name ?: album.title ?: "Unknown Album",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = titleModifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = album.artist ?: "Unknown Artist",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            val category = album.category
            if (!category.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors =
                    CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = "Album info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
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
}

@Composable
private fun Modifier.breathingAlbumBackground(): Modifier {
    val colorScheme = MaterialTheme.colorScheme
    val infiniteTransition = rememberInfiniteTransition(label = "album-background")
    val pulse by
        infiniteTransition.animateFloat(
            initialValue = 0.82f,
            targetValue = 1.18f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 4200),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "album-background-pulse",
        )

    return drawBehind {
        drawRect(colorScheme.surface)
        drawRect(
            brush =
                Brush.radialGradient(
                    colors =
                        listOf(
                            colorScheme.primaryContainer.copy(alpha = 0.36f * pulse),
                            Color.Transparent,
                        ),
                    center = Offset(size.width * 0.12f, size.height * 0.08f),
                    radius = size.maxDimension * 0.78f * pulse,
                ),
        )
        drawRect(
            brush =
                Brush.radialGradient(
                    colors =
                        listOf(
                            colorScheme.tertiaryContainer.copy(alpha = 0.28f * pulse),
                            Color.Transparent,
                        ),
                    center = Offset(size.width * 0.96f, size.height * 0.38f),
                    radius = size.maxDimension * 0.62f * pulse,
                ),
        )
        drawRect(
            brush =
                Brush.radialGradient(
                    colors =
                        listOf(
                            colorScheme.secondaryContainer.copy(alpha = 0.22f * (2f - pulse)),
                            Color.Transparent,
                        ),
                    center = Offset(size.width * 0.44f, size.height * 1.02f),
                    radius = size.maxDimension * 0.7f * (2f - pulse),
                ),
        )
    }
}

@Composable
private fun AlbumDetailRow(
    label: String,
    value: String?,
) {
    if (value.isNullOrBlank()) return

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
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
            text = "Loading album details...",
            modifier = Modifier.padding(top = 16.dp),
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
fun ErrorStatePreview() {
    ErrorState(message = "An error occurred while fetching the album details. Please try again later.")
}
