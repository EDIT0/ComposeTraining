package com.my.book.library.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.my.book.library.data.repository.local.AppDatabase
import com.my.book.library.data.repository.local.LocalDataSource
import com.my.book.library.data.repository.local.LocalDataSourceImpl
import com.my.book.library.data.repository.local.dao.BookDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "book_library.db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesBookDetailDao(appDatabase: AppDatabase): BookDetailDao {
        return appDatabase.bookDetailDao()
    }

    @Singleton
    @Provides
    fun providesLocalDataSource(
        bookDetailDao: BookDetailDao,
        gson: Gson
    ): LocalDataSource {
        return LocalDataSourceImpl(bookDetailDao = bookDetailDao, gson = gson)
    }
}
