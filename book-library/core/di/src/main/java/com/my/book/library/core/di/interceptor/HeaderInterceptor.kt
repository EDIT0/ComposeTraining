package com.my.book.library.core.di.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Accept", "*/*")
            .addHeader("Content-Type", "application/json")

        return chain.proceed(newRequest.build())
    }
}