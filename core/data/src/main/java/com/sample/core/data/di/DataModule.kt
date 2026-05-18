package com.sample.core.data.di

import com.sample.core.data.repository.AlbumsRepository
import com.sample.core.data.repository.AlbumsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    internal abstract fun bindsAlbumsRepository(albumsRepository: AlbumsRepositoryImpl): AlbumsRepository
}
