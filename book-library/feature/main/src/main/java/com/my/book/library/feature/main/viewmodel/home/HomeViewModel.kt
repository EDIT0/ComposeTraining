package com.my.book.library.feature.main.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {



}