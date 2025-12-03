package com.my.book.library.feature.select_library.library.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.domain.usecase.GetSearchDetailRegionBookLibraryUseCase
import com.my.book.library.feature.select_library.library.intent.SelectLibraryListUiEvent
import com.my.book.library.feature.select_library.library.intent.SelectLibraryListViewModelEvent
import com.my.book.library.feature.select_library.library.state.SelectLibraryListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectLibraryListViewModel @Inject constructor(
    app: Application,
    private val getSearchDetailRegionBookLibraryUseCase: GetSearchDetailRegionBookLibraryUseCase
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(selectLibraryListViewModelEvent: SelectLibraryListViewModelEvent) {
        when(selectLibraryListViewModelEvent) {
            is SelectLibraryListViewModelEvent.UpdateDetailRegion -> {
                viewModelScope.launch {
                    _selectLibraryListUiEvent.send(SelectLibraryListUiEvent.UpdateDetailRegion(detailRegion = selectLibraryListViewModelEvent.detailRegion))
                }
            }
            is SelectLibraryListViewModelEvent.RequestLibraryList -> {
                viewModelScope.launch {
                    requestLibrary(detailRegion = selectLibraryListUiState.value.detailRegion!!)
                }
            }
        }
    }

    private val _selectLibraryListUiEvent = Channel<SelectLibraryListUiEvent>(capacity = Channel.UNLIMITED)
    val selectLibraryListUiState: StateFlow<SelectLibraryListUiState> = _selectLibraryListUiEvent.receiveAsFlow()
        .runningFold(
            initial = SelectLibraryListUiState(),
            operation = { state, event ->
                when(event) {
                    is SelectLibraryListUiEvent.UpdateDetailRegion -> {
                        state.copy(detailRegion = event.detailRegion)
                    }
                    is SelectLibraryListUiEvent.UpdateLibraryList -> {
                        state.copy(libraryList = MutableStateFlow(value = event.libraryList!!))
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, SelectLibraryListUiState())

    private suspend fun requestLibrary(detailRegion: LibraryData.DetailRegion) {
        getSearchDetailRegionBookLibraryUseCase.invokePaging(
            reqSearchDetailRegionBookLibrary = ReqSearchDetailRegionBookLibrary(
                pageNo = 0,
                pageSize = 0,
                dtlRegion = detailRegion.code
            )
        )
            .map { it ->
                it.map {
                    LogUtil.d_dev("페이징 데이터:  ${it}")
                    it
                }
            }
            .cachedIn(viewModelScope)
            .catch { e ->
                _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message?:""))
            }
            .collect {
                _selectLibraryListUiEvent.send(SelectLibraryListUiEvent.UpdateLibraryList(libraryList = it))
            }
    }

}