package com.my.book.library.feature.select_library.library_detail.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectLibraryListDetailViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

}