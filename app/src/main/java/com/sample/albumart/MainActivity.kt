package com.sample.albumart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sample.albumart.ui.theme.AlbumArtTheme
import com.sample.feature.albumdetails.api.navigation.AlbumDetailsNavKey
import com.sample.feature.albumdetails.impl.navigation.albumDetailsEntry
import com.sample.feature.albumlist.api.navigation.AlbumListNavKey
import com.sample.feature.albumlist.impl.navigation.albumListEntry
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AlbumArtTheme {
                SharedTransitionLayout {
                    val backStack = remember { mutableStateListOf<NavKey>(AlbumListNavKey) }
                    val navigateBack = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }

                    Scaffold(
                        topBar = {
                            AlbumArtTopBar(
                                currentKey = backStack.lastOrNull(),
                                canNavigateBack = backStack.size > 1,
                                onNavigateBack = navigateBack,
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.surface,
                    ) { innerPadding ->
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .background(
                                        Brush.verticalGradient(
                                            colors =
                                                listOf(
                                                    MaterialTheme.colorScheme.surfaceContainerLow,
                                                    MaterialTheme.colorScheme.surface,
                                                    MaterialTheme.colorScheme.surfaceContainerLowest,
                                                ),
                                        ),
                                    ),
                        ) {
                            NavDisplay(
                                backStack = backStack,
                                onBack = navigateBack,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                transitionSpec = {
                                    fadeIn(animationSpec = tween(220)) togetherWith
                                        fadeOut(animationSpec = tween(160))
                                },
                                popTransitionSpec = {
                                    fadeIn(animationSpec = tween(180)) togetherWith
                                        fadeOut(animationSpec = tween(80))
                                },
                                predictivePopTransitionSpec = {
                                    fadeIn(animationSpec = tween(180)) togetherWith
                                        fadeOut(animationSpec = tween(80))
                                },
                                entryProvider =
                                    entryProvider {
                                        albumListEntry(
                                            backStack = backStack,
                                            // Shared Transition Scope for album cover animation on open and close
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                        )
                                        albumDetailsEntry(
                                            // Shared Transition Scope for album cover animation on open and close
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                        )
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumArtTopBar(
    currentKey: NavKey?,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
) {
    val albumDetailsKey = currentKey as? AlbumDetailsNavKey
    val title = albumDetailsKey?.bandName?.takeIf { it.isNotBlank() } ?: "Album Art"

    TopAppBar(
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    BackArrow()
                }
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = colorResource(id = R.color.ic_launcher_background),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Album Art app icon",
                        modifier = Modifier.size(38.dp),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style =
                        MaterialTheme.typography.titleLarge.copy(
                            fontSize =
                                when {
                                    title.length > 42 -> 16.sp
                                    title.length > 30 -> 18.sp
                                    title.length > 22 -> 20.sp
                                    else -> 22.sp
                                },
                            lineHeight =
                                when {
                                    title.length > 42 -> 20.sp
                                    title.length > 30 -> 22.sp
                                    title.length > 22 -> 24.sp
                                    else -> 28.sp
                                },
                        ),
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
            ),
    )
}

@Composable
private fun BackArrow() {
    val color = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = 2.8.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.66f, size.height * 0.18f),
            end = Offset(size.width * 0.34f, size.height * 0.5f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.34f, size.height * 0.5f),
            end = Offset(size.width * 0.66f, size.height * 0.82f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
    }
}
