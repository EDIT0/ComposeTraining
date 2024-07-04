package com.my.presentation.screen.base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.my.common.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseAndroidViewModel @Inject constructor(
    val app: Application,
    val networkManager: NetworkManager
) : AndroidViewModel(application = app) {

}