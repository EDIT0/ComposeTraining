package com.my.book.library.feature.select_library.library.state

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData
import kotlinx.coroutines.flow.MutableStateFlow

data class SelectLibraryListUiState(
    val detailRegion: LibraryData.DetailRegion? = null,
    val libraryList: MutableStateFlow<PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>>? = null
)