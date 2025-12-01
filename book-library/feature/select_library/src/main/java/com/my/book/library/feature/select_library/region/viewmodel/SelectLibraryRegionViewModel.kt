package com.my.book.library.feature.select_library.region.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.my.book.library.feature.select_library.region.intent.SelectLibraryRegionViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SelectLibraryRegionViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {

    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(selectLibraryRegionViewModelEvent: SelectLibraryRegionViewModelEvent) {
        when(selectLibraryRegionViewModelEvent) {

            else -> {}
        }
    }


}