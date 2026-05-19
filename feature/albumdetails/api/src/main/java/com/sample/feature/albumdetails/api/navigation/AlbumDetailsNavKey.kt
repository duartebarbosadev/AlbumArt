package com.sample.feature.albumdetails.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDetailsNavKey(
    val albumId: String,
    val bandName: String? = null,
) : NavKey
