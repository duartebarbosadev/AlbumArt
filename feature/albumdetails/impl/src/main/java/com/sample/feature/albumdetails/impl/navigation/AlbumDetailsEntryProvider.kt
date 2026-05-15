package com.sample.feature.albumdetails.impl.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.sample.feature.albumdetails.api.navigation.AlbumDetailsNavKey
import com.sample.feature.albumdetails.impl.AlbumDetailsScreen


fun EntryProviderScope<NavKey>.albumDetailsEntry() {

    // Is ID the best param? because we don't do any subsequent queries, so maybe we can just pass the whole details object?
    entry<AlbumDetailsNavKey> { key ->
        AlbumDetailsScreen(key.id)
    }
}