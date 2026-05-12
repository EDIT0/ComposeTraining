package com.my.book.library.feature.main.state.home

import com.my.book.library.core.model.local.MyRegionAndLibrary

data class HomeUiState(
    val myLibraryInfo: MyRegionAndLibrary? = null
)