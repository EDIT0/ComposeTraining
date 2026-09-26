package com.my.book.library.feature.main.state.rank_detail

import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResRecommendList

data class BookRankDetailUiState(
    val isbn13: String = "",
    val isLoading: Boolean = true,
    val bookWrapper: ResBookDetail.ResponseData.BookWrapper? = null,
    val recommendList: List<ResRecommendList.ResponseData.BookWrapper>? = null
)