package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

/**
 * 대출 급상승 도서 조회 응답
 */
data class ResHotTrend(
    @SerializedName("response")
    val response: ResponseData?
) {
    data class ResponseData(
        @SerializedName("request")
        val request: RequestData?,
        @SerializedName("results")
        val results: List<ResultWrapper>? // 일자별 결과 목록
    ) {
        data class RequestData(
            @SerializedName("searchDt")
            val searchDt: String?,
            @SerializedName("format")
            val format: String?
        )

        data class ResultWrapper(
            @SerializedName("result")
            val result: ResultData? // 일자별 결과
        ) {
            data class ResultData(
                @SerializedName("date")
                val date: String?, // 기준일자
                @SerializedName("docs")
                val docs: List<DocWrapper>? // 목록
            ) {
                data class DocWrapper(
                    @SerializedName("doc")
                    val doc: DocData? // 도서
                ) {
                    data class DocData(
                        @SerializedName("no")
                        val no: Int?, // 순번
                        @SerializedName("difference")
                        val difference: Int?, // 대출순위 상승폭
                        @SerializedName("baseWeekRank")
                        val baseWeekRank: Int?, // 기준일 순위
                        @SerializedName("pastWeekRank")
                        val pastWeekRank: Int?, // 전주 순위
                        @SerializedName("bookname")
                        val bookname: String?, // 도서명
                        @SerializedName("authors")
                        val authors: String?, // 저자명
                        @SerializedName("publisher")
                        val publisher: String?, // 출판사
                        @SerializedName("publication_year")
                        val publicationYear: String?, // 출판년도
                        @SerializedName("isbn13")
                        val isbn13: String?, // 13자리 ISBN
                        @SerializedName("addition_symbol")
                        val additionSymbol: String?, // ISBN 부가기호
                        @SerializedName("vol")
                        val vol: String?, // 권
                        @SerializedName("class_no")
                        val classNo: String?, // 주제분류
                        @SerializedName("class_nm")
                        val classNm: String?, // 주제분류명
                        @SerializedName("bookImageURL")
                        val bookImageURL: String?, // 책표지 URL
                        @SerializedName("bookDtlUrl")
                        val bookDtlUrl: String? // 도서 상세 페이지 URL
                    )
                }
            }
        }
    }
}
