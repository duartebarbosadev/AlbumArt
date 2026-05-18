package com.sample.core.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.sample.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
/**
 * Provides network related dependencies
 * The OkHttpClient is configured to use Chucker in debug builds
 */
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    val collector =
                        ChuckerCollector(
                            context = context,
                            showNotification = true,
                            retentionPeriod = RetentionManager.Period.ONE_HOUR,
                        )
                    val chuckerInterceptor =
                        ChuckerInterceptor
                            .Builder(context)
                            .collector(collector)
                            .maxContentLength(250_000L)
                            .alwaysReadResponseBody(true)
                            .createShortcut(true)
                            .build()
                    addInterceptor(chuckerInterceptor)
                }
            }.build()
}
