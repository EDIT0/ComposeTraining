package com.example.movieappdemo1.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.movieappdemo1.data.db.MovieDAO
import com.example.movieappdemo1.data.db.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideMovieDatabase(app : Application) : MovieDatabase {
        return Room.databaseBuilder(app, MovieDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase) : MovieDAO {
        return movieDatabase.getMovieDAO()
    }
}