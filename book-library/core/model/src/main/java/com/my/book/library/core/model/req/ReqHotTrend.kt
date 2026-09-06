package com.my.book.library.core.model.req

/**
 * 대출 급상승 도서 조회 요청
 *
 * @param searchDt 검색일자 (yyyy-MM-dd)
 */
data class ReqHotTrend(
    val searchDt: String
)
