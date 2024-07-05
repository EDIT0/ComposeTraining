package com.my.presentation.screen.moviesearch.viewmodel

import android.app.Application
import com.my.common.NetworkManager
import com.my.presentation.screen.base.viewmodel.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    app: Application,
    networkManager: NetworkManager
) : BaseAndroidViewModel(app, networkManager){



}