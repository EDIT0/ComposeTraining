package com.my.book.library.featrue.splash.intro.intent

sealed interface SplashViewModelEvent {
    class CheckMyLibraryInfo(): SplashViewModelEvent
}

sealed interface SplashUiEvent {

}