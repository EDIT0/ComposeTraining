package com.my.book.library.feature.splash.intro.intent

sealed interface SplashViewModelEvent {
    class CheckMyLibraryInfo(): SplashViewModelEvent
}

sealed interface SplashUiEvent {

}