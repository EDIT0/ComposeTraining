package com.my.book.library.feature.select_region.guide.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.domain.usecase.data_store.GetMyLibraryInfoUseCase
import com.my.book.library.feature.select_region.guide.intent.RegionSelectGuideViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionSelectGuideViewModel @Inject constructor(
    app: Application,
    private val getMyLibraryInfoUseCase: GetMyLibraryInfoUseCase
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        data object NavigateToMain : SideEffectEvent
        data object NavigateBack : SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(regionSelectGuideViewModelEvent: RegionSelectGuideViewModelEvent) {
        when(regionSelectGuideViewModelEvent) {
            is RegionSelectGuideViewModelEvent.CheckRegionAndBack -> {
                viewModelScope.launch {
                    getMyLibraryInfoUseCase.invoke().collect { result ->
                        when (result) {
                            is RequestResult.Success -> {
                                _sideEffectEvent.send(SideEffectEvent.NavigateToMain)
                            }
                            else -> {
                                _sideEffectEvent.send(SideEffectEvent.NavigateBack)
                            }
                        }
                    }
                }
            }
        }
    }

}