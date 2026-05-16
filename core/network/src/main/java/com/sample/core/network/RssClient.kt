package com.sample.core.network

import com.sample.core.network.model.ItunesRssResponseDto
import jakarta.inject.Inject
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

// TODO Instead of getting always 100, investigate if it supports pagination
private const val ITUNES_TOP_ALBUMS_RSS_URL = "https://itunes.apple.com/us/rss/topalbums/limit=100/json"

class RssClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {

    fun getItunesRss(): ItunesRssResponseDto {
        val request = Request.Builder()
            .url(ITUNES_TOP_ALBUMS_RSS_URL)
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val body = response.body.string()
            return Json.decodeFromString<ItunesRssResponseDto>(body)
        }
    }
}