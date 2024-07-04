package com.my.data.di

import com.my.data.api.TmdbAPIService
import com.my.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.my.data.repository.remoteDataSourceImpl.MoviesRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    @Singleton
    fun provideMoviesRemoteDataSource(tmdbAPIService: TmdbAPIService) : MoviesRemoteDataSource {
        return MoviesRemoteDataSourceImpl(tmdbAPIService)
    }

}