package com.my.book.library

import android.app.Application
import com.my.book.library.core.common.util.DataStoreUtil
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App: Application() {

    @Inject
    lateinit var dataStoreUtil: DataStoreUtil

    override fun onCreate() {
        super.onCreate()

        dataStoreUtil.initialize(applicationContext)

    }
}