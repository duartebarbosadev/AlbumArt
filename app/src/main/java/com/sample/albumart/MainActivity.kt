package com.sample.albumart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sample.albumart.ui.theme.AlbumArtTheme
import com.sample.feature.albumlist.api.navigation.AlbumListNavKey
import com.sample.feature.albumlist.impl.navigation.albumListEntry
import com.sample.feature.albumdetails.impl.navigation.albumDetailsEntry
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalSharedTransitionApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO Splashscreen use it to wait for album cover loading

        enableEdgeToEdge()
        setContent {
            AlbumArtTheme {
                SharedTransitionLayout {
                    val backStack = remember { mutableStateListOf<NavKey>(AlbumListNavKey) }

                    NavDisplay(
                        backStack = backStack,
                        onBack = {
                            if (backStack.isNotEmpty()) {
                                backStack.removeLastOrNull()
                            }
                        },
                        entryProvider = entryProvider {
                            albumListEntry(
                                backStack = backStack,
                                sharedTransitionScope = this@SharedTransitionLayout,
                            )
                            albumDetailsEntry(
                                sharedTransitionScope = this@SharedTransitionLayout,
                            )
                        }
                    )
                }
            }
        }
    }
}
