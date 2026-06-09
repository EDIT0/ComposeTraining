package com.my.book.library.feature.search.library.state

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResCheckBookAvailability
import com.my.book.library.core.model.res.ResLibraryBookData
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData
import kotlinx.coroutines.flow.MutableStateFlow

data class LibraryMapUiState(
    val currentDetailRegion: LibraryData.DetailRegion? = null,
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val libraryList: MutableStateFlow<PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>>? = null,
    val holdingLibraryList: MutableStateFlow<PagingData<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>>? = null,
    val selectedLibCode: String? = null,
    val sheetOffsetRatio: Float = -1f,
    val resCheckBookAvailability: ResCheckBookAvailability? = null,
    val resLibraryBookData: ResLibraryBookData? = null,
    val isLibraryBookDataLoading: Boolean = false
)