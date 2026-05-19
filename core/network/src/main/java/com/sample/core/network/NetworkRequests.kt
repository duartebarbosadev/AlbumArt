package com.sample.core.network

import android.content.Context
import com.sample.core.network.model.ItunesRssResponseDto
import com.sample.network.R
import dagger.hilt.android.qualifiers.ApplicationContext
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
        @ApplicationContext private val context: Context,
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
                            throw IOException(context.getString(R.string.error_request_failed))
                        }

                        val body = response.body.string()
                        Json.decodeFromString<ItunesRssResponseDto>(body)
                    }
                } catch (exception: SerializationException) {
                    throw IOException(
                        context.getString(R.string.error_network_unreachable),
                        exception,
                    )
                }
            }
    }
