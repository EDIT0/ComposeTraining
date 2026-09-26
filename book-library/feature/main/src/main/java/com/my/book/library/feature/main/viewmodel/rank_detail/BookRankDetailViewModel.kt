package com.my.book.library.feature.main.viewmodel.rank_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqBookDetail
import com.my.book.library.core.model.req.ReqRecommendList
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResRecommendList
import com.my.book.library.domain.usecase.GetBookDetailUseCase
import com.my.book.library.domain.usecase.GetRecommendListUseCase
import com.my.book.library.feature.main.intent.rank_detail.BookRankDetailUiEvent
import com.my.book.library.feature.main.intent.rank_detail.BookRankDetailViewModelEvent
import com.my.book.library.feature.main.state.rank_detail.BookRankDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookRankDetailViewModel @Inject constructor(
    app: Application,
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val getRecommendListUseCase: GetRecommendListUseCase
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    private val _bookRankDetailUiEvent = Channel<BookRankDetailUiEvent>(capacity = Channel.UNLIMITED)
    val bookRankDetailUiState: StateFlow<BookRankDetailUiState> = _bookRankDetailUiEvent.receiveAsFlow()
        .runningFold(
            initial = BookRankDetailUiState(),
            operation = { state, event ->
                when(event) {
                    is BookRankDetailUiEvent.UpdateIsbn13 -> {
                        state.copy(isbn13 = event.isbn13)
                    }
                    is BookRankDetailUiEvent.UpdateLoading -> {
                        state.copy(isLoading = event.isLoading)
                    }
                    is BookRankDetailUiEvent.UpdateBookDetail -> {
                        state.copy(bookWrapper = event.bookWrapper)
                    }
                    is BookRankDetailUiEvent.UpdateRecommendList -> {
                        state.copy(recommendList = event.recommendList)
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookRankDetailUiState())

    fun intentAction(bookRankDetailViewModelEvent: BookRankDetailViewModelEvent) {

        when(bookRankDetailViewModelEvent) {
            is BookRankDetailViewModelEvent.LoadBookDetailAndRecommendList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateLoading(isLoading = true))
                    try {
                        loadBookDetail(isbn13 = bookRankDetailViewModelEvent.isbn13)
                        loadRecommendList(isbn13 = bookRankDetailViewModelEvent.isbn13)
                    } finally {
                        _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateLoading(isLoading = false))
                    }
                }
            }
        }
    }

    private suspend fun loadBookDetail(isbn13: String) {
        getBookDetailUseCase.invoke(
            reqBookDetail = ReqBookDetail(isbn13 = isbn13)
        )
            .filter {
                if(it is RequestResult.Error) {
                    _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateBookDetail(bookWrapper = null))
                }

                return@filter it is RequestResult.Success
            }
            .catch {
                _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateBookDetail(bookWrapper = null))
            }
            .collect {
                val detail: ResBookDetail.ResponseData.BookWrapper? = it.resultData?.response?.detail?.get(0)
                _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateBookDetail(bookWrapper = detail))
            }
    }

    private suspend fun loadRecommendList(isbn13: String) {
        getRecommendListUseCase.invoke(
            reqRecommendList = ReqRecommendList(isbn13 = isbn13)
        )
            .filter {
                if(it is RequestResult.Error) {
                    _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateRecommendList(recommendList = null))
                }

                return@filter it is RequestResult.Success
            }
            .catch {
                _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateRecommendList(recommendList = null))
            }
            .collect {
                val docs: List<ResRecommendList.ResponseData.BookWrapper>? = it.resultData?.response?.docs
                _bookRankDetailUiEvent.send(BookRankDetailUiEvent.UpdateRecommendList(recommendList = docs))
            }
    }


}