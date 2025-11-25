package com.my.book.library.core.di

import com.google.gson.GsonBuilder
import com.my.book.library.core.common.Constant
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.di.interceptor.HeaderInterceptor
import com.my.book.library.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // Interceptor
    // OkHttpClient
    // Retrofit

    @Singleton
    @Provides
    fun providesHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor(
            logger = {
                LogUtil.i_dev("[Network] ${it}")
            }
        ).run {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val builder = OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor) // 네트워크 레벨까지 다 찍히도록 변경
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)

        return builder.build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .create()

        val builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)

        return builder.build()
    }
}