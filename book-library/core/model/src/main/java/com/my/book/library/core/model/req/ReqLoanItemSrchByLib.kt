package com.my.book.library.core.model.req

/**
 * 도서관/지역별 인기대출도서 조회 요청 (지역 코드 기반)
 *
 * @param region 지역 코드
 * @param dtlRegion 세부지역 코드
 * @param pageNo 페이지번호
 * @param pageSize 페이지크기
 */
data class ReqLoanItemSrchByLib(
    val region: Int,
    val dtlRegion: Int,
    val pageNo: Int,
    val pageSize: Int
)
