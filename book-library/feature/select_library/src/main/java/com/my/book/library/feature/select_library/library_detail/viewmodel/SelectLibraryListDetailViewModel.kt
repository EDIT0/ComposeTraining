package com.my.book.library.feature.select_library.library_detail.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.resource.R
import com.my.book.library.domain.usecase.data_store.SetMyLibraryInfoUseCase
import com.my.book.library.feature.select_library.library_detail.intent.SelectLibraryListDetailUiEvent
import com.my.book.library.feature.select_library.library_detail.intent.SelectLibraryListDetailViewModelEvent
import com.my.book.library.feature.select_library.library_detail.state.SelectLibraryListDetailUiState
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
class SelectLibraryListDetailViewModel @Inject constructor(
    private val app: Application,
    private val setMyLibraryInfoUseCase: SetMyLibraryInfoUseCase
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
        class OnSuccessRegistration(): SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    fun intentAction(selectLibraryListDetailViewModelEvent: SelectLibraryListDetailViewModelEvent) {
        when(selectLibraryListDetailViewModelEvent) {
            is SelectLibraryListDetailViewModelEvent.UpdateDetailRegionAndLibrary -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _selectLibraryListDetailUiEvent.send(SelectLibraryListDetailUiEvent.UpdateDetailRegion(detailRegion = selectLibraryListDetailViewModelEvent.detailRegion))
                    _selectLibraryListDetailUiEvent.send(SelectLibraryListDetailUiEvent.UpdateLibrary(library = selectLibraryListDetailViewModelEvent.library))
                }
            }
            is SelectLibraryListDetailViewModelEvent.RegisterRegionAndLibrary -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if(selectLibraryListDetailUiState.value.detailRegion != null && selectLibraryListDetailUiState.value.library != null) {
                        setMyLibraryInfoUseCase.invoke(myRegionAndLibrary = MyRegionAndLibrary(
                            detailRegion = selectLibraryListDetailUiState.value.detailRegion!!,
                            library = selectLibraryListDetailUiState.value.library!!
                        ))
                            .filter {
                                if(it is RequestResult.Error) {
                                    // 저장 실패
                                    _sideEffectEvent.send(SideEffectEvent.ShowToast(message = app.getString(R.string.select_library_list_detail_register_failure)))
                                }

                                return@filter it is RequestResult.Success
                            }
                            .catch {
                                // 오류 처리
                                _sideEffectEvent.send(SideEffectEvent.ShowToast(message = it.message.toString()))
                            }
                            .collect {
                                // 저장 성공
                                _sideEffectEvent.send(SideEffectEvent.OnSuccessRegistration())
                            }
                    } else {
                        // 데이터 중 Null 있음
                        _sideEffectEvent.send(SideEffectEvent.ShowToast(message = app.getString(R.string.select_library_list_detail_register_data_empty)))
                    }
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