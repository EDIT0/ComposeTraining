package com.my.book.library.core.di

import com.my.book.library.core.common.util.DataStoreUtil
import com.my.book.library.data.repository.DataStoreRepositoryImpl
import com.my.book.library.data.repository.RepositoryImpl
import com.my.book.library.data.repository.remote.RemoteDataSource
import com.my.book.library.domain.repository.DataStoreRepository
import com.my.book.library.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesRepository(
        remoteDataSource: RemoteDataSource
    ): Repository {
        return RepositoryImpl(remoteDataSource = remoteDataSource)
    }

    @Singleton
    @Provides
    fun providesDataStoreRepository(
        dataStoreUtil: DataStoreUtil
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStoreUtil = dataStoreUtil)
    }
}