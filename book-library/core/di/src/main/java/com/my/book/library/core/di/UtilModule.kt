package com.my.book.library.core.di

import com.my.book.library.core.common.util.DataStoreUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Singleton
    @Provides
    fun providesDataStoreUtil(): DataStoreUtil {
        return DataStoreUtil()
    }

}