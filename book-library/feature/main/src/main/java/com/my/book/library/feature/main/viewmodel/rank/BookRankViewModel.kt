package com.my.book.library.feature.main.viewmodel.rank

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.my.book.library.core.model.local.BookRankType
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqHotTrend
import com.my.book.library.core.model.req.ReqLoanItemSrchByLib
import com.my.book.library.domain.usecase.GetHotTrendUseCase
import com.my.book.library.domain.usecase.GetLoanItemSrchByLibUseCase
import com.my.book.library.domain.usecase.data_store.GetMyLibraryInfoUseCase
import com.my.book.library.feature.main.intent.rank.BookRankUiEvent
import com.my.book.library.feature.main.intent.rank.BookRankViewModelEvent
import com.my.book.library.feature.main.state.rank.BookRankUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val POPULAR_LOAN_PAGE_SIZE = 20

@HiltViewModel
class BookRankViewModel @Inject constructor(
    app: Application,
    private val getMyLibraryInfoUseCase: GetMyLibraryInfoUseCase,
    private val getHotTrendUseCase: GetHotTrendUseCase,
    private val getLoanItemSrchByLibUseCase: GetLoanItemSrchByLibUseCase
): AndroidViewModel(application = app) {

    private val _bookRankUiEvent = Channel<BookRankUiEvent>(capacity = Channel.UNLIMITED)
    val bookRankUiState: StateFlow<BookRankUiState> = _bookRankUiEvent.receiveAsFlow()
        .runningFold(
            initial = BookRankUiState(),
            operation = { state, event ->
                when (event) {
                    is BookRankUiEvent.UpdateBookRankType -> {
                        state.copy(bookRankType = event.bookRankType)
                    }
                    is BookRankUiEvent.UpdateHotTrendBooks -> {
                        state.copy(hotTrendBooks = event.hotTrendBooks)
                    }
                    is BookRankUiEvent.UpdatePopularLoanBooks -> {
                        state.copy(popularLoanBooks = MutableStateFlow(value = event.popularLoanBooks))
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookRankUiState())

    fun intentAction(event: BookRankViewModelEvent) {
        when (event) {
            is BookRankViewModelEvent.LoadBookRank -> {
                viewModelScope.launch(Dispatchers.Main) {
                    _bookRankUiEvent.send(BookRankUiEvent.UpdateBookRankType(event.bookRankType))
                }

                when (event.bookRankType) {
                    BookRankType.HOT_TREND -> loadHotTrend()
                    BookRankType.POPULAR_LOAN -> loadPopularLoanBooks()
                }
            }
        }
    }

    private fun loadHotTrend() {
        viewModelScope.launch(Dispatchers.IO) {
            getHotTrendUseCase.invoke(reqHotTrend = ReqHotTrend(searchDt = LocalDate.now().toString()))
                .collect { result ->
                    when (result) {
                        is RequestResult.Success -> {
                            _bookRankUiEvent.send(BookRankUiEvent.UpdateHotTrendBooks(result.resultData ?: emptyList()))
                        }
                        else -> {
                            _bookRankUiEvent.send(BookRankUiEvent.UpdateHotTrendBooks(emptyList()))
                        }
                    }
                }
        }
    }

    private fun loadPopularLoanBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            getMyLibraryInfoUseCase.invoke().collect { libraryResult ->
                val detailRegion = (libraryResult as? RequestResult.Success)?.resultData?.detailRegion
                if (detailRegion == null) {
                    _bookRankUiEvent.send(BookRankUiEvent.UpdatePopularLoanBooks(PagingData.empty()))
                    return@collect
                }

                getLoanItemSrchByLibUseCase.invokePaging(
                    reqLoanItemSrchByLib = ReqLoanItemSrchByLib(
                        region = detailRegion.regionCode,
                        dtlRegion = detailRegion.code,
                        pageNo = 1,
                        pageSize = POPULAR_LOAN_PAGE_SIZE
                    )
                )
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _bookRankUiEvent.send(BookRankUiEvent.UpdatePopularLoanBooks(pagingData))
                    }
            }
        }
    }
}
