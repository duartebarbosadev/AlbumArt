package com.sample.feature.albumdetails.impl.navigation




import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.sample.feature.albumdetails.api.navigation.AlbumDetailsNavKey
import com.sample.feature.albumdetails.impl.AlbumDetailsScreen
import com.sample.feature.albumdetails.impl.AlbumDetailsViewModel

fun EntryProviderScope<NavKey>.albumDetailsEntry() {

    entry<AlbumDetailsNavKey> { key ->

        val viewModel: AlbumDetailsViewModel = hiltViewModel(
            key = key.albumId,
            creationCallback = { factory: AlbumDetailsViewModel.Factory ->
                factory.create(key.albumId)
            }
        )

        AlbumDetailsScreen(
            viewModel = viewModel,
        )
    }
}