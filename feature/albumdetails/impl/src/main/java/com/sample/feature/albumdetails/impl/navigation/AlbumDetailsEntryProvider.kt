package com.sample.feature.albumdetails.impl.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.sample.feature.albumdetails.api.navigation.AlbumDetailsNavKey
import com.sample.feature.albumdetails.impl.AlbumDetailsScreen
import com.sample.feature.albumdetails.impl.AlbumDetailsViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
fun EntryProviderScope<NavKey>.albumDetailsEntry(
    sharedTransitionScope: SharedTransitionScope,
) {

    entry<AlbumDetailsNavKey> { key ->

        val viewModel: AlbumDetailsViewModel = hiltViewModel(
            key = key.albumId,
            creationCallback = { factory: AlbumDetailsViewModel.Factory ->
                factory.create(key.albumId)
            }
        )

        AlbumDetailsScreen(
            viewModel = viewModel,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
        )
    }
}
