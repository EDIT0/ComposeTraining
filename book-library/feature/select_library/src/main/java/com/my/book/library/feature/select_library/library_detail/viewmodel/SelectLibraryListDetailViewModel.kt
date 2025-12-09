package com.my.book.library.feature.select_library.library_detail.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.feature.select_library.library_detail.intent.SelectLibraryListDetailUiEvent
import com.my.book.library.feature.select_library.library_detail.intent.SelectLibraryListDetailViewModelEvent
import com.my.book.library.feature.select_library.library_detail.state.SelectLibraryListDetailUiState
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
class SelectLibraryListDetailViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(selectLibraryListDetailViewModelEvent: SelectLibraryListDetailViewModelEvent) {
        when(selectLibraryListDetailViewModelEvent) {
            is SelectLibraryListDetailViewModelEvent.UpdateDetailRegionAndLibrary -> {
                viewModelScope.launch {
                    _selectLibraryListDetailUiEvent.send(SelectLibraryListDetailUiEvent.UpdateDetailRegion(detailRegion = selectLibraryListDetailViewModelEvent.detailRegion))
                    _selectLibraryListDetailUiEvent.send(SelectLibraryListDetailUiEvent.UpdateLibrary(library = selectLibraryListDetailViewModelEvent.library))
                }
            }
        }
    }

    private val _selectLibraryListDetailUiEvent = Channel<SelectLibraryListDetailUiEvent>(capacity = Channel.UNLIMITED)
    val selectLibraryListDetailUiState: StateFlow<SelectLibraryListDetailUiState> = _selectLibraryListDetailUiEvent.receiveAsFlow()
        .runningFold(
            initial = SelectLibraryListDetailUiState(),
            operation = { state, event ->
                when(event) {
                    is SelectLibraryListDetailUiEvent.UpdateDetailRegion -> {
                        state.copy(detailRegion = event.detailRegion)
                    }
                    is SelectLibraryListDetailUiEvent.UpdateLibrary -> {
                        state.copy(library = event.library)
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, SelectLibraryListDetailUiState())
}