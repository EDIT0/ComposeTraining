package com.my.book.library.core.di

import com.my.book.library.data.api.ApiService
import com.my.book.library.data.repository.remote.RemoteDataSource
import com.my.book.library.data.repository.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        apiService: ApiService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(apiService = apiService)
    }

}