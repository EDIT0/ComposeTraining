package com.my.book.library.feature.search.book.intent

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResSearchBook

sealed interface SearchViewModelEvent {
    data class RequestUpdateTitle(val title: String): SearchViewModelEvent
    data class RequestSearchBook(val title: String): SearchViewModelEvent
}

sealed interface SearchUiEvent {
    data class UpdateTitle(val title: String): SearchUiEvent
    data class UpdateBookList(val bookList: PagingData<ResSearchBook.ResponseData.BookWrapper>?): SearchUiEvent
    data class UpdateSearchInfo(val searchInfo: ResSearchBook.ResponseData): SearchUiEvent
}