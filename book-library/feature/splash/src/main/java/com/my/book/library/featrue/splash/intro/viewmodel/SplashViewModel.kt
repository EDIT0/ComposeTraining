package com.my.book.library.featrue.splash.intro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.my.book.library.core.common.util.PrefUtil
import com.my.book.library.core.model.res.ResSearchBookLibrary

import com.my.book.library.featrue.splash.intro.intent.SplashViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    app: Application
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
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
                    isCheckMyLibraryInfoExist(prefUnit = PrefUtil)
                }
            }
        }
    }

    /**
     * 내 도서관 정보 유무 확인
     *
     * @param prefUnit
     */
    private suspend fun isCheckMyLibraryInfoExist(prefUnit: PrefUtil) {
        val resSearchBookLibrary: ResSearchBookLibrary? = prefUnit.getJson(key = PrefUtil.MY_LIBRARY_INFO, type = ResSearchBookLibrary::class.java)
        if(resSearchBookLibrary != null) {
            // 도서관 정보 있음
            _sideEffectEvent.send(SideEffectEvent.OnMoveToMain())
        } else {
            // 도서관 정보 없음
            _sideEffectEvent.send(SideEffectEvent.OnMoveToSelectLibrary())
        }
    }
}