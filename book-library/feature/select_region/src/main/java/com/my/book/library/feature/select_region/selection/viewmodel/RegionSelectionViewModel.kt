package com.my.book.library.feature.select_region.selection.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.feature.select_region.selection.intent.RegionSelectionUiEvent
import com.my.book.library.feature.select_region.selection.intent.RegionSelectionViewModelEvent
import com.my.book.library.feature.select_region.selection.state.RegionSelectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionSelectionViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {

    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(regionSelectionViewModelEvent: RegionSelectionViewModelEvent) {
        when(regionSelectionViewModelEvent) {
            is RegionSelectionViewModelEvent.SetSelectionRegion -> {
                viewModelScope.launch(Dispatchers.Main) {
                    _regionSelectionUiEvent.send(RegionSelectionUiEvent.UpdateSelectionRegion(region = regionSelectionViewModelEvent.region))
                }
            }
            is RegionSelectionViewModelEvent.SetSelectionDetailRegion -> {
                viewModelScope.launch(Dispatchers.Main) {
                    _regionSelectionUiEvent.send(RegionSelectionUiEvent.UpdateSelectionDetailRegion(detailRegion = regionSelectionViewModelEvent.detailRegion))
                }
            }
        }
    }

    private val _regionSelectionUiEvent = Channel<RegionSelectionUiEvent>(capacity = Channel.UNLIMITED)
    val regionSelectionUiState: StateFlow<RegionSelectionUiState> = _regionSelectionUiEvent.receiveAsFlow()
        .runningFold(
            initial = RegionSelectionUiState(),
            operation = { state, event ->
                when(event) {
                    is RegionSelectionUiEvent.UpdateSelectionRegion -> {
                        state.copy(region = event.region)
                    }
                    is RegionSelectionUiEvent.UpdateSelectionDetailRegion -> {
                        state.copy(detailRegion = event.detailRegion)
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, RegionSelectionUiState())
}