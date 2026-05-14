package com.my.book.library.feature.search.book.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.req.ReqSearchBookWithKeyword
import com.my.book.library.domain.usecase.GetSearchBookWithKeywordUseCase
import com.my.book.library.feature.search.book.intent.SearchUiEvent
import com.my.book.library.feature.search.book.intent.SearchViewModelEvent
import com.my.book.library.feature.search.book.state.SearchUiState
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
class SearchViewModel @Inject constructor(
    private val app: Application,
    private val getSearchBookWithKeywordUseCase: GetSearchBookWithKeywordUseCase
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(searchViewModelEvent: SearchViewModelEvent) {
        when(searchViewModelEvent) {
            is SearchViewModelEvent.RequestUpdateKeyword -> {
                viewModelScope.launch {
                    _searchUiEvent.send(SearchUiEvent.UpdateKeyword(searchViewModelEvent.keyword))
                }
            }
            is SearchViewModelEvent.RequestSearchBook -> {
                viewModelScope.launch {
                    requestSearchBook(keyword = searchViewModelEvent.keyword)
                }
            }
        }
    }

    private val _searchUiEvent = Channel<SearchUiEvent>(capacity = Channel.UNLIMITED)
    val searchUiState: StateFlow<SearchUiState> = _searchUiEvent.receiveAsFlow()
        .runningFold(
            initial = SearchUiState(),
            operation = { state, event ->
                when(event) {
                    is SearchUiEvent.UpdateKeyword -> {
                        state.copy(keyword = event.keyword)
                    }
                    is SearchUiEvent.UpdateBookList -> {
                        state.copy(bookList = MutableStateFlow(value = event.bookList!!))
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchUiState())

    private suspend fun requestSearchBook(keyword: String) {
        getSearchBookWithKeywordUseCase.invokePaging(
            reqSearchBookWithKeyword = ReqSearchBookWithKeyword(
                pageNo = 0,
                pageSize = 0,
                keyword = keyword
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
                _searchUiEvent.send(SearchUiEvent.UpdateBookList(bookList = it))
            }
    }

}