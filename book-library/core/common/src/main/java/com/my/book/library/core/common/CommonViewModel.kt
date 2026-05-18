package com.my.book.library.core.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.my.book.library.core.common.util.NetworkConnectivityUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class CommonViewModel @Inject constructor(
    app: Application,
    networkConnectivityUtil: NetworkConnectivityUtil
): AndroidViewModel(application = app) {

    // Network 상태
    val isNetworkConnected: StateFlow<Boolean> = networkConnectivityUtil.isConnected

}