package com.my.book.feature.search_library.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.feature.search_library.intent.SearchLibraryUiEvent
import com.my.book.feature.search_library.intent.SearchLibraryViewModelEvent
import com.my.book.feature.search_library.state.SearchLibraryUiState
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.domain.usecase.GetSearchRegionBookLibraryUseCase
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
class SearchLibraryViewModel @Inject constructor(
    app: Application,
    private val getSearchRegionBookLibraryUseCase: GetSearchRegionBookLibraryUseCase
): AndroidViewModel(application = app) {

    /**
     * UI Setting State
     */
    private val _searchLibraryUiEvent = Channel<SearchLibraryUiEvent>(capacity = Channel.UNLIMITED)
    val searchLibraryUiState: StateFlow<SearchLibraryUiState> = _searchLibraryUiEvent.receiveAsFlow()
        .runningFold(
            initial = SearchLibraryUiState(),
            operation = { state, event ->
                when(event) {
                    is SearchLibraryUiEvent.UpdateIsFilterOpen -> {
                        state.copy(isFilterOpen = event.isFilterOpen)
                    }
                    is SearchLibraryUiEvent.UpdateSelectionRegion -> {
                        state.copy(selectionRegion = event.selectionRegion)
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchLibraryUiState())

    /**
     * ViewModel로 요청하는 Event
     *
     * @param searchLibraryViewModelEvent
     */
    fun intentAction(searchLibraryViewModelEvent: SearchLibraryViewModelEvent) {
        when(searchLibraryViewModelEvent) {
            is SearchLibraryViewModelEvent.RequestGtSearchRegionBookLibrary -> {
                requestGtSearchRegionBookLibrary(
                    region = searchLibraryViewModelEvent.region,
                    pageNo = searchLibraryViewModelEvent.pageNo,
                    pageSize = searchLibraryViewModelEvent.pageSize
                )
            }
            is SearchLibraryViewModelEvent.SetIsFilterOpen -> {
                viewModelScope.launch {
                    _searchLibraryUiEvent.send(SearchLibraryUiEvent.UpdateIsFilterOpen(isFilterOpen = searchLibraryViewModelEvent.isFilterOpen))
                }
            }
            is SearchLibraryViewModelEvent.SetSelectionRegion -> {
                viewModelScope.launch {
                    _searchLibraryUiEvent.send(SearchLibraryUiEvent.UpdateSelectionRegion(selectionRegion = searchLibraryViewModelEvent.selectionRegion))
                }
            }
        }
    }

    private fun requestGtSearchRegionBookLibrary(region: Int, pageNo: Int, pageSize: Int) {
        viewModelScope.launch {
            getSearchRegionBookLibraryUseCase.invoke(
                reqSearchRegionBookLibrary = ReqSearchRegionBookLibrary(
                    pageNo = pageNo,
                    pageSize = pageSize,
                    region = region
                )
            ).collect {

            }
        }
    }

}