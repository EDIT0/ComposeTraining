package com.my.book.library.feature.search.library.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.domain.usecase.GetSearchDetailRegionBookLibraryUseCase
import com.my.book.library.feature.search.library.intent.LibraryMapUiEvent
import com.my.book.library.feature.search.library.intent.LibraryMapViewModelEvent
import com.my.book.library.feature.search.library.state.LibraryMapUiState
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
class LibraryMapViewModel @Inject constructor(
    private val app: Application,
    private val getSearchDetailRegionBookLibraryUseCase: GetSearchDetailRegionBookLibraryUseCase
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    private val _libraryMapUiEvent = Channel<LibraryMapUiEvent>(capacity = Channel.UNLIMITED)
    val libraryMapUiState: StateFlow<LibraryMapUiState> = _libraryMapUiEvent.receiveAsFlow()
        .runningFold(
            initial = LibraryMapUiState(),
            operation = { state, event ->
                when(event) {
                    is LibraryMapUiEvent.UpdateLibraryList -> {
                        state.copy(libraryList = MutableStateFlow(value = event.libraryList!!))
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, LibraryMapUiState())

    fun intentAction(libraryMapViewModelEvent: LibraryMapViewModelEvent) {
        when(libraryMapViewModelEvent) {
            is LibraryMapViewModelEvent.RequestLibraryList -> {
                viewModelScope.launch {
                    requestLibrary(detailRegion = libraryMapViewModelEvent.detailRegion)
                }
            }
        }
    }

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
                    LogUtil.d_dev("페이징 데이터: ${it}")
                    it
                }
            }
            .cachedIn(viewModelScope)
            .catch { e ->
                _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message?:""))
            }
            .collect {
                _libraryMapUiEvent.send(LibraryMapUiEvent.UpdateLibraryList(libraryList = it))
            }
    }

}