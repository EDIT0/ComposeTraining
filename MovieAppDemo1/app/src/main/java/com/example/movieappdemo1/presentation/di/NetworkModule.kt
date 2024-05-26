package com.example.movieappdemo1.presentation.di

import android.content.Context
import com.example.movieappdemo1.BuildConfig
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.data.api.TmdbAPIService
import com.example.movieappdemo1.presentation.util.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
                LogUtil.i_dev(message)
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