package com.example.movieappdemo1.presentation.di

import com.example.movieappdemo1.data.repository.MoviesRepositoryImpl
import com.example.movieappdemo1.data.repository.localDataSource.MovieLocalDataSource
import com.example.movieappdemo1.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.example.movieappdemo1.domain.repository.MoviesRepository
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
        movieLocalDataSource: MovieLocalDataSource
    ) : MoviesRepository {
        return MoviesRepositoryImpl(moviesRemoteDataSource, movieLocalDataSource)
    }

}