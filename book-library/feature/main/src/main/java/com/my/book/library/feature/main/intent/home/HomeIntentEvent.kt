package com.my.book.library.feature.main.intent.home

import com.my.book.library.core.model.local.MyRegionAndLibrary

sealed interface HomeViewModelEvent {
    data object GetMyLibraryInfo : HomeViewModelEvent
}

sealed interface HomeUiEvent {
    data class UpdateMyLibraryInfo(val myLibraryInfo: MyRegionAndLibrary?) : HomeUiEvent
}
