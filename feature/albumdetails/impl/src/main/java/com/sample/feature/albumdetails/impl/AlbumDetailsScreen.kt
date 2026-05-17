package com.sample.feature.albumdetails.impl


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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sample.core.data.model.Album
import com.sample.core.ui.DevicePreviews

@Composable
fun AlbumDetailsScreen(
    viewModel: AlbumDetailsViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    when (val uiState = uiState) {
        is AlbumDetailsUiState.Loading -> {
            Box(modifier = Modifier) {
                Text("Loading...")
            }
        }

        is AlbumDetailsUiState.Success -> {
            AlbumDetail(
                album = uiState.album
            )
        }

        is AlbumDetailsUiState.Error -> {
            Box(modifier = Modifier) {
                Text("Error: ${uiState.message}")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetail(album: Album) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Album Details")
                },
            )
        }
    ) { innerPadding ->
        AlbumDetailContent(
            album = album,
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun AlbumDetailContent(
    album: Album,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = album.largeImageURL ?: album.imageURL,
                contentDescription = album.name ?: album.title ?: "Album cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
            // TODO Maybe a background diffusion color based on the album cover's dominant color
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = album.name ?: album.title ?: "Unknown Album",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = album.artist ?: "Unknown Artist",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
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

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}

@DevicePreviews
@Composable
fun AlbumDetailsScreenPreview() {
    AlbumDetail(
        album = Album(
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