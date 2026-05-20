package com.my.book.library.feature.search.library.state

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResSearchBookLibrary
import kotlinx.coroutines.flow.MutableStateFlow

data class LibraryMapUiState(
    val libraryList: MutableStateFlow<PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>>? = null
)