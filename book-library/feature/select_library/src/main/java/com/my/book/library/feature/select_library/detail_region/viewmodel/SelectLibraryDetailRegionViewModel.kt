package com.my.book.library.feature.select_library.detail_region.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.feature.select_library.detail_region.intent.SelectLibraryDetailRegionUiEvent
import com.my.book.library.feature.select_library.detail_region.intent.SelectLibraryDetailRegionViewModelEvent
import com.my.book.library.feature.select_library.detail_region.state.SelectLibraryDetailRegionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectLibraryDetailRegionViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {

    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(selectLibraryDetailRegionViewModelEvent: SelectLibraryDetailRegionViewModelEvent) {
        when(selectLibraryDetailRegionViewModelEvent) {
            is SelectLibraryDetailRegionViewModelEvent.UpdateRegion -> {
                viewModelScope.launch {
                    _selectLibraryDetailRegionUiEvent.send(SelectLibraryDetailRegionUiEvent.UpdateRegion(region = selectLibraryDetailRegionViewModelEvent.region))
                }
            }
        }
    }

    private val _selectLibraryDetailRegionUiEvent = Channel<SelectLibraryDetailRegionUiEvent>(capacity = Channel.UNLIMITED)
    val selectLibraryDetailRegionUiState: StateFlow<SelectLibraryDetailRegionUiState> = _selectLibraryDetailRegionUiEvent.receiveAsFlow()
        .runningFold(
            initial = SelectLibraryDetailRegionUiState(),
            operation = { state, event ->
                when(event) {
                    is SelectLibraryDetailRegionUiEvent.UpdateRegion -> {
                        state.copy(region = event.region)
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, SelectLibraryDetailRegionUiState())

}