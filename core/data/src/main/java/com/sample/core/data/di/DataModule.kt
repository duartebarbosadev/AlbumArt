package com.sample.core.data.di

import com.sample.core.data.repository.AlbumsRepository
import com.sample.core.data.repository.AlbumsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsAlbumsRepository(
        albumsRepository: AlbumsRepositoryImpl,
    ): AlbumsRepository
}