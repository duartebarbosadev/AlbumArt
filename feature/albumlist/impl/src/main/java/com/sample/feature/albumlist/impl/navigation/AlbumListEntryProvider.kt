package com.sample.feature.albumlist.impl.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.sample.feature.albumdetails.api.navigation.AlbumDetailsNavKey
import com.sample.feature.albumlist.api.navigation.AlbumListNavKey
import com.sample.feature.albumlist.impl.AlbumListScreen

@OptIn(ExperimentalSharedTransitionApi::class)
fun EntryProviderScope<NavKey>.albumListEntry(
    backStack: SnapshotStateList<NavKey>,
    sharedTransitionScope: SharedTransitionScope,
) {
    entry<AlbumListNavKey> {
        AlbumListScreen(
            onAlbumClick = { album ->
                album.id?.let { albumId ->
                    backStack.add(
                        AlbumDetailsNavKey(
                            albumId = albumId,
                            bandName = album.artist,
                        ),
                    )
                }
            },
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
        )
    }
}
