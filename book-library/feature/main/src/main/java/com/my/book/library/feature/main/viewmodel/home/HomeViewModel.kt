package com.my.book.library.feature.main.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

    val testState = MutableStateFlow<String>("TEST")

    fun changeTestState(str: String) {
        testState.value = str
    }
}