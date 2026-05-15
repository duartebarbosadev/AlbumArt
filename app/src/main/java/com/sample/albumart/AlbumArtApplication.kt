package com.sample.albumart

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlbumArtApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO use strict mode
    }
}