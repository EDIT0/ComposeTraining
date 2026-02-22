package com.my.book.library.feature.main.viewmodel.save

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {



}