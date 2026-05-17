package com.sample.feature.albumdetails.api.navigation

import androidx.navigation3.runtime.NavKey
import com.sample.core.data.model.Album
import kotlinx.serialization.Serializable

@Serializable
// Is ID the best param? because we don't do any subsequent queries, so maybe we can just pass the whole details object?
data class AlbumDetailsNavKey(val album: Album): NavKey