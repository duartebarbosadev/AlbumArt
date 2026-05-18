package com.sample.feature.albumlist.impl

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.sample.core.data.model.Album
import org.junit.Rule
import org.junit.Test

class AlbumItemTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun displaysAlbumTitleArtistAndCover() {
        val album =
            testAlbum(
                title = EXPECTED_TITLE,
                name = EXPECTED_NAME,
                artist = EXPECTED_ARTIST,
            )

        composeRule.setContent {
            AlbumItem(
                album = album,
                onClick = {},
            )
        }

        composeRule.onNodeWithText(EXPECTED_TITLE).assertIsDisplayed()
        composeRule.onNodeWithText(EXPECTED_ARTIST).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(EXPECTED_NAME).assertIsDisplayed()
    }

    @Test
    fun hidesArtistWhenTitleAlreadyContainsArtist() {
        composeRule.setContent {
            AlbumItem(
                album =
                    testAlbum(
                        title = "$EXPECTED_TITLE - $EXPECTED_ARTIST",
                        artist = EXPECTED_ARTIST,
                    ),
                onClick = {},
            )
        }

        composeRule.onNodeWithText("$EXPECTED_TITLE - $EXPECTED_ARTIST").assertIsDisplayed()
        composeRule.onNodeWithText(EXPECTED_ARTIST).assertDoesNotExist()
    }

    private fun testAlbum(
        id: String = EXPECTED_ID,
        title: String = EXPECTED_TITLE,
        name: String = EXPECTED_NAME,
        artist: String = EXPECTED_ARTIST,
    ) = Album(
        id = id,
        title = title,
        name = name,
        artist = artist,
        imageURL = null,
        largeImageURL = null,
        itemCount = null,
        price = null,
        currency = null,
        contentType = null,
        rights = null,
        releaseDate = null,
        category = null,
        albumUrl = null,
    )

    private companion object {
        const val EXPECTED_ID = "1"
        const val EXPECTED_TITLE = "Album 1"
        const val EXPECTED_NAME = "Album Name 1"
        const val EXPECTED_ARTIST = "Artist 1"
    }
}
