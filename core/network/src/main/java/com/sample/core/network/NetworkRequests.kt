package com.sample.core.network

import com.sample.core.network.model.ItunesRssResponseDto
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

// TODO Instead of getting always 100, investigate if it supports pagination
private const val ITUNES_TOP_ALBUMS_RSS_URL = "https://itunes.apple.com/us/rss/topalbums/limit=100/json"

class NetworkRequests
    @Inject
    constructor(
        private val okHttpClient: OkHttpClient,
    ) {
        suspend fun getItunesRss(): ItunesRssResponseDto =
            withContext(Dispatchers.IO) {
                val request =
                    Request
                        .Builder()
                        .url(ITUNES_TOP_ALBUMS_RSS_URL)
                        .build()

                try {
                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            throw IOException("There was an issue with the request, try again later")
                        }

                        val body = response.body.string()
                        Json.decodeFromString<ItunesRssResponseDto>(body)
                    }
                } catch (exception: SerializationException) {
                    throw IOException(
                        "Could not reach Itunes. Check your internet connection and try again.",
                        exception,
                    )
                }
            }
    }
