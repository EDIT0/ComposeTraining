package com.my.data.di

import com.my.data.repository.MoviesRepositoryImpl
import com.my.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.my.domain.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        moviesRemoteDataSource: MoviesRemoteDataSource,
    ) : MoviesRepository {
        return MoviesRepositoryImpl(moviesRemoteDataSource)
    }

}