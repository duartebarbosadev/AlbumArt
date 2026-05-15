package com.sample.feature.albumlist.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AlbumListScreen(
    onAlbumClick : (albumId : String) -> Unit
) {
    Box(modifier = Modifier) {
        Text("Album Cover")
    }
}