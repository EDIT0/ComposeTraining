package com.my.book.library.feature.select_region.guide.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.my.book.library.feature.select_region.guide.intent.RegionSelectGuideViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RegionSelectGuideViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {

    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(regionSelectGuideViewModelEvent: RegionSelectGuideViewModelEvent) {
        when(regionSelectGuideViewModelEvent) {

            else -> {}
        }
    }

}