package com.sample.feature.albumdetails.impl

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.sample.core.data.model.Album
import org.junit.Rule
import org.junit.Test

class AlbumDetailTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun displaysAlbumDetails() {
        setAlbumDetailContent(testAlbum())

        composeRule.onNodeWithText(EXPECTED_NAME).assertIsDisplayed()
        composeRule.onNodeWithText(EXPECTED_ARTIST).assertIsDisplayed()

        composeRule.onAllNodesWithText(EXPECTED_CATEGORY)[0].assertIsDisplayed()
        composeRule.onNodeWithText(EXPECTED_RELEASE_DATE).performScrollTo().assertIsDisplayed()
        composeRule.onAllNodesWithText(EXPECTED_ITEM_COUNT)[0].assertIsDisplayed()
        composeRule.onAllNodesWithText(EXPECTED_PRICE)[0].assertIsDisplayed()
        composeRule.onAllNodesWithText(EXPECTED_CONTENT_TYPE)[0].assertIsDisplayed()
        composeRule.onNodeWithText(EXPECTED_RIGHTS).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun hidesBlankOptionalRows() {
        setAlbumDetailContent(
            testAlbum(
                category = null,
                releaseDate = "",
                itemCount = null,
                price = "",
                contentType = null,
                rights = "",
            ),
        )

        composeRule.onNodeWithText("Category").assertDoesNotExist()
        composeRule.onNodeWithText("Release date").assertDoesNotExist()
        composeRule.onNodeWithText("Copyright").assertDoesNotExist()
    }

    private fun testAlbum(
        category: String? = EXPECTED_CATEGORY,
        releaseDate: String? = EXPECTED_RELEASE_DATE,
        itemCount: String? = EXPECTED_ITEM_COUNT,
        price: String? = EXPECTED_PRICE,
        contentType: String? = EXPECTED_CONTENT_TYPE,
        rights: String? = EXPECTED_RIGHTS,
    ) = Album(
        id = EXPECTED_ID,
        title = EXPECTED_TITLE,
        name = EXPECTED_NAME,
        artist = EXPECTED_ARTIST,
        imageURL = null,
        largeImageURL = null,
        itemCount = itemCount,
        price = price,
        currency = null,
        contentType = contentType,
        rights = rights,
        releaseDate = releaseDate,
        category = category,
        albumUrl = null,
    )

    private fun setAlbumDetailContent(album: Album) {
        composeRule.setContent {
            AlbumDetail(album = album)
        }
        composeRule.waitForIdle()
    }

    private companion object {
        const val EXPECTED_ID = "1"
        const val EXPECTED_TITLE = "Album 1 - Artist 1"
        const val EXPECTED_NAME = "Album 1"
        const val EXPECTED_ARTIST = "Artist 1"
        const val EXPECTED_CATEGORY = "Alternative"
        const val EXPECTED_RELEASE_DATE = "May 18, 2026"
        const val EXPECTED_ITEM_COUNT = "12"
        const val EXPECTED_PRICE = "$9.99"
        const val EXPECTED_CONTENT_TYPE = "Album"
        const val EXPECTED_RIGHTS = "2026 Example Records"
    }
}
