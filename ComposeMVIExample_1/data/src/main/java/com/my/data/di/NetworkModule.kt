package com.my.data.di

import android.content.Context
import android.util.Log
import com.my.common.NetworkManager
import com.my.data.BuildConfig
import com.my.data.api.TmdbAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
//        setHeader: SetHeader
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                Log.d("MYTAG","[Network] : ${message}")
            }
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }
//        builder.addNetworkInterceptor(setHeader)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideTmdbAPIService(retrofit: Retrofit) : TmdbAPIService {
        return retrofit.create(TmdbAPIService::class.java)
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) : Context {
        return context
    }

    @Singleton
    @Provides
    fun provideNetworkManager(context: Context) : NetworkManager {
        return NetworkManager(context)
    }
}