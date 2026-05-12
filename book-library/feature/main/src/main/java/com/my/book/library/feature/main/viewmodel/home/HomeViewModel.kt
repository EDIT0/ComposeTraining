package com.my.book.library.feature.main.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.model.network.RequestResult
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    app: Application,
    private val getMyLibraryInfoUseCase: GetMyLibraryInfoUseCase
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
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())

    init {
        intentAction(HomeViewModelEvent.GetMyLibraryInfo)
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
        }
    }
}
