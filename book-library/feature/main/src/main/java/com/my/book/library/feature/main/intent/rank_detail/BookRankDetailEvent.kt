package com.my.book.library.feature.main.intent.rank_detail

import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResRecommendList

sealed interface BookRankDetailViewModelEvent {
    data class LoadBookDetailAndRecommendList(val isbn13: String): BookRankDetailViewModelEvent
}

sealed interface BookRankDetailUiEvent {
    data class UpdateIsbn13(val isbn13: String): BookRankDetailUiEvent
    data class UpdateLoading(val isLoading: Boolean): BookRankDetailUiEvent
    data class UpdateBookDetail(val bookWrapper: ResBookDetail.ResponseData.BookWrapper?): BookRankDetailUiEvent
    data class UpdateRecommendList(val recommendList: List<ResRecommendList.ResponseData.BookWrapper>?): BookRankDetailUiEvent
}