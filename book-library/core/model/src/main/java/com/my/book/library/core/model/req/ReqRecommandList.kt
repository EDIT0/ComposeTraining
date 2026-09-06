package com.my.book.library.core.model.req

/**
 * 마니아를 위한 추천도서 조회 요청
 *
 * @param isbn13 10자리 또는 13자리 ISBN
 * @param type 추천유형 (mania: 마니아를 위한 추천도서)
 */
data class ReqRecommandList(
    val isbn13: String,
    val type: String = "mania"
)
