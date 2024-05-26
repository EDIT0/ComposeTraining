package com.example.movieappdemo1.presentation.di

import com.example.movieappdemo1.domain.repository.MoviesRepository
import com.example.movieappdemo1.domain.usecase.DeleteSavedMovieUseCase
import com.example.movieappdemo1.domain.usecase.GetPopularMoviesUseCase
import com.example.movieappdemo1.domain.usecase.GetSavedMoviesUseCase
import com.example.movieappdemo1.domain.usecase.GetSearchMoviesUseCase
import com.example.movieappdemo1.domain.usecase.GetSearchSavedMoviesUseCase
import com.example.movieappdemo1.domain.usecase.SaveMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(moviesRepository: MoviesRepository) : GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(moviesRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchMoviesUseCase(moviesRepository: MoviesRepository) : GetSearchMoviesUseCase {
        return GetSearchMoviesUseCase(moviesRepository)
    }

    @Singleton
    @Provides
    fun provideSaveMovieUseCase(moviesRepository: MoviesRepository) : SaveMovieUseCase {
        return SaveMovieUseCase(moviesRepository)
    }

    @Singleton
    @Provides
    fun provideGetSavedMoviesUseCase(moviesRepository: MoviesRepository) : GetSavedMoviesUseCase {
        return GetSavedMoviesUseCase(moviesRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSavedMovieUseCase(moviesRepository: MoviesRepository) : DeleteSavedMovieUseCase {
        return DeleteSavedMovieUseCase(moviesRepository)
    }

    @Singleton
    @Provides
    fun provideGetSearchSavedMoviesUseCase(moviesRepository: MoviesRepository) : GetSearchSavedMoviesUseCase {
        return GetSearchSavedMoviesUseCase(moviesRepository)
    }
}