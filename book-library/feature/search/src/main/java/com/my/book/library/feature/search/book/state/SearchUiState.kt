package com.my.book.library.feature.search.book.state

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResSearchBook
import kotlinx.coroutines.flow.MutableStateFlow

data class SearchUiState(
    val title: String = "",
    val bookList: MutableStateFlow<PagingData<ResSearchBook.ResponseData.BookWrapper>>? = null,
    val searchInfo: ResSearchBook.ResponseData? = null
)