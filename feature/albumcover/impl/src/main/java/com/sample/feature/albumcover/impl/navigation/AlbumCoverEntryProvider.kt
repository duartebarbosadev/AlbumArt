package com.sample.feature.albumcover.impl.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.sample.feature.albumcover.api.navigation.AlbumCoverNavKey
import com.sample.feature.albumcover.impl.AlbumCoverScreen
import com.sample.feature.albumdetails.api.navigation.AlbumDetailsNavKey

fun EntryProviderScope<NavKey>.albumCoverEntry(backStack : SnapshotStateList<NavKey>) {
    entry<AlbumCoverNavKey> {
        AlbumCoverScreen(
            onAlbumClick = { albumId ->
                backStack.add(AlbumDetailsNavKey(albumId))
            }
        )
    }
}