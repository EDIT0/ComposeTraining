package com.my.book.library.featrue.splash.intro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.domain.usecase.data_store.GetMyLibraryInfoUseCase
import com.my.book.library.featrue.splash.intro.intent.SplashViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val app: Application,
    private val getMyLibraryInfoUseCase: GetMyLibraryInfoUseCase
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
        class OnMoveToMain(): SideEffectEvent
        class OnMoveToSelectLibrary(): SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    /**
     * ViewModel로 요청하는 Event
     *
     * @param splashViewModelEvent
     */
    fun intentAction(splashViewModelEvent: SplashViewModelEvent) {
        when(splashViewModelEvent) {
            is SplashViewModelEvent.CheckMyLibraryInfo -> {
                viewModelScope.launch {
                    isCheckMyLibraryInfoExist()
                }
            }
        }
    }

    /**
     * 내 도서관 정보 유무 확인
     *
     */
    private suspend fun isCheckMyLibraryInfoExist() {
        getMyLibraryInfoUseCase.invoke()
            .filter {
                if(it is RequestResult.Error) {
                    // 도서관 정보 없음
                    _sideEffectEvent.send(SideEffectEvent.OnMoveToSelectLibrary())
                }

                return@filter it is RequestResult.Success
            }
            .catch {
                // 오류 처리
                _sideEffectEvent.send(SideEffectEvent.ShowToast(message = it.message.toString()))
            }
            .collect {
                // 도서관 정보 있음
                _sideEffectEvent.send(SideEffectEvent.OnMoveToMain())
            }
    }
}