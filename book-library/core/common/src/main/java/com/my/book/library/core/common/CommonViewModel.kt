package com.my.book.library.core.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {



}