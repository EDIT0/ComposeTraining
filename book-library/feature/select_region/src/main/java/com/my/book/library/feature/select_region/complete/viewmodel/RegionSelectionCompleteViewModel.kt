package com.my.book.library.feature.select_region.complete.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.resource.R
import com.my.book.library.domain.usecase.data_store.SetMyLibraryInfoUseCase
import com.my.book.library.feature.select_region.complete.intent.RegionSelectionCompleteUiEvent
import com.my.book.library.feature.select_region.complete.intent.RegionSelectionCompleteViewModelEvent
import com.my.book.library.feature.select_region.complete.state.RegionSelectionCompleteUiState
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
class RegionSelectionCompleteViewModel @Inject constructor(
   private val app: Application,
   private val setMyLibraryInfoUseCase: SetMyLibraryInfoUseCase
): AndroidViewModel(application = app){

   sealed interface SideEffectEvent {
      class ShowToast(val message: String): SideEffectEvent
      class OnSuccessRegistration(): SideEffectEvent
   }

   private val _sideEffectEvent = Channel<SideEffectEvent>()
   val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

   fun intentAction(regionSelectionCompleteViewModelEvent: RegionSelectionCompleteViewModelEvent) {
      when(regionSelectionCompleteViewModelEvent) {
         is RegionSelectionCompleteViewModelEvent.SetDetailRegion -> {
            viewModelScope.launch(Dispatchers.Main) {
               _regionSelectionCompleteUiEvent.send(RegionSelectionCompleteUiEvent.UpdateDetailRegion(detailRegion = regionSelectionCompleteViewModelEvent.detailRegion))
            }
         }
         is RegionSelectionCompleteViewModelEvent.RegisterRegionAndLibrary -> {
            viewModelScope.launch(Dispatchers.IO) {
               registerRegion()
            }
         }
      }
   }

   private val _regionSelectionCompleteUiEvent = Channel<RegionSelectionCompleteUiEvent>(capacity = Channel.UNLIMITED)
   val regionSelectionCompleteUiState: StateFlow<RegionSelectionCompleteUiState> = _regionSelectionCompleteUiEvent.receiveAsFlow()
      .runningFold(
         initial = RegionSelectionCompleteUiState(),
         operation = { state, event ->
            when(event) {
               is RegionSelectionCompleteUiEvent.UpdateDetailRegion -> {
                  state.copy(detailRegion = event.detailRegion)
               }
            }
         }
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, RegionSelectionCompleteUiState())

   /**
    * 선택한 지역 로컬에 저장
    *
    */
   private suspend fun registerRegion() {
      if(regionSelectionCompleteUiState.value.detailRegion != null) {
         setMyLibraryInfoUseCase.invoke(
            myRegionAndLibrary = MyRegionAndLibrary(
               detailRegion = regionSelectionCompleteUiState.value.detailRegion!!,
               library = null // 내 도서관 기획 변경
            )
         )
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