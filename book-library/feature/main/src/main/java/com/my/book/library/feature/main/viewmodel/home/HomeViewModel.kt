package com.my.book.library.feature.main.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqHotTrend
import com.my.book.library.core.model.req.ReqLoanItemSrchByLib
import com.my.book.library.domain.usecase.GetHotTrendUseCase
import com.my.book.library.domain.usecase.GetLoanItemSrchByLibUseCase
import com.my.book.library.domain.usecase.data_store.GetMyLibraryInfoUseCase
import com.my.book.library.feature.main.intent.home.HomeUiEvent
import com.my.book.library.feature.main.intent.home.HomeViewModelEvent
import com.my.book.library.feature.main.state.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    app: Application,
    private val getMyLibraryInfoUseCase: GetMyLibraryInfoUseCase,
    private val getHotTrendUseCase: GetHotTrendUseCase,
    private val getLoanItemSrchByLibUseCase: GetLoanItemSrchByLibUseCase
): AndroidViewModel(application = app) {

    private val _homeUiEvent = Channel<HomeUiEvent>(capacity = Channel.UNLIMITED)
    val homeUiState: StateFlow<HomeUiState> = _homeUiEvent.receiveAsFlow()
        .runningFold(
            initial = HomeUiState(),
            operation = { state, event ->
                when (event) {
                    is HomeUiEvent.UpdateMyLibraryInfo -> {
                        state.copy(myLibraryInfo = event.myLibraryInfo)
                    }
                    is HomeUiEvent.UpdateHotTrendBooks -> {
                        state.copy(hotTrendBooks = event.hotTrendBooks)
                    }
                    is HomeUiEvent.UpdatePopularLoanBooks -> {
                        state.copy(popularLoanBooks = event.popularLoanBooks)
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())

    init {
        intentAction(HomeViewModelEvent.GetMyLibraryInfo)
//        intentAction(HomeViewModelEvent.GetHotTrend)
    }

    fun intentAction(event: HomeViewModelEvent) {
        when (event) {
            is HomeViewModelEvent.GetMyLibraryInfo -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getMyLibraryInfoUseCase.invoke()
                        .collect { result ->
                            when (result) {
                                is RequestResult.Success -> {
                                    _homeUiEvent.send(HomeUiEvent.UpdateMyLibraryInfo(result.resultData))
                                }
                                else -> {
                                    _homeUiEvent.send(HomeUiEvent.UpdateMyLibraryInfo(null))
                                }
                            }
                        }
                }
            }
            is HomeViewModelEvent.GetHotTrend -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getHotTrendUseCase.invoke(reqHotTrend = ReqHotTrend(searchDt = LocalDate.now().toString()))
                        .collect { result ->
                            when (result) {
                                is RequestResult.Success -> {
                                    // 홈 캐러셀은 페이징 없이 최대 10권만 고정으로 보여준다
                                    _homeUiEvent.send(HomeUiEvent.UpdateHotTrendBooks((result.resultData ?: emptyList()).take(10)))
                                }
                                else -> {
                                    _homeUiEvent.send(HomeUiEvent.UpdateHotTrendBooks(emptyList()))
                                }
                            }
                        }
                }
            }
            is HomeViewModelEvent.GetPopularLoanBooks -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getMyLibraryInfoUseCase.invoke().collect { libraryResult ->
                        val detailRegion = (libraryResult as? RequestResult.Success)?.resultData?.detailRegion
                        if (detailRegion == null) {
                            _homeUiEvent.send(HomeUiEvent.UpdatePopularLoanBooks(emptyList()))
                            return@collect
                        }

                        getLoanItemSrchByLibUseCase.invoke(
                            reqLoanItemSrchByLib = ReqLoanItemSrchByLib(
                                region = detailRegion.regionCode,
                                dtlRegion = detailRegion.code,
                                pageNo = 1,
                                pageSize = 12
                            )
                        ).collect { result ->
                            when (result) {
                                is RequestResult.Success -> {
                                    // 홈 섹션은 페이징 없이 최대 12권만 고정으로 보여준다
                                    val books = result.resultData?.response?.docs
                                        ?.mapNotNull { it.doc }
                                        .orEmpty()
                                        .take(12)
                                    _homeUiEvent.send(HomeUiEvent.UpdatePopularLoanBooks(books))
                                }
                                else -> {
                                    _homeUiEvent.send(HomeUiEvent.UpdatePopularLoanBooks(emptyList()))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
