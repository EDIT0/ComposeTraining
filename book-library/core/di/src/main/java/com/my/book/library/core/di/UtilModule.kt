package com.my.book.library.core.di

import android.content.Context
import com.my.book.library.core.common.util.DataStoreUtil
import com.my.book.library.core.common.util.LocationUtil
import com.my.book.library.core.common.util.NetworkConnectivityUtil
import com.my.book.library.core.common.util.PermissionUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Singleton
    @Provides
    fun providesDataStoreUtil(
        @ApplicationContext context: Context
    ): DataStoreUtil {
        return DataStoreUtil().apply {
            initialize(context)
        }
    }

    @Singleton
    @Provides
    fun providesNetworkConnectivityUtil(
        @ApplicationContext context: Context
    ): NetworkConnectivityUtil {
        return NetworkConnectivityUtil(context)
    }

    @Singleton
    @Provides
    fun providesPermissionUtil(
        @ApplicationContext context: Context
    ): PermissionUtil {
        return PermissionUtil(context)
    }

    @Singleton
    @Provides
    fun providesLocationUtil(
        @ApplicationContext context: Context,
        permissionUtil: PermissionUtil
    ): LocationUtil {
        return LocationUtil(context, permissionUtil)
    }

}