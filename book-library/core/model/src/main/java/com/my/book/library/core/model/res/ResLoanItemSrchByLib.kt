package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

/**
 * 도서관/지역별 인기대출도서 조회 응답
 */
data class ResLoanItemSrchByLib(
    @SerializedName("response")
    val response: ResponseData?
) {
    data class ResponseData(
        @SerializedName("libNm")
        val libNm: String?, // 도서관명 (도서관 기준으로 조회했을 경우)
        @SerializedName("regionNm")
        val regionNm: String?, // 지역명 (지역 기준으로 조회했을 경우)
        @SerializedName("dtlregionNm")
        val dtlregionNm: String?, // 세부지역명 (세부지역 기준으로 조회했을 경우)
        @SerializedName("resultNum")
        val resultNum: Int?, // 응답결과 건수
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
                @SerializedName("ranking")
                val ranking: String?, // 순위
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
                val bookDtlUrl: String?, // 도서 상세 페이지 URL
                @SerializedName("loan_count")
                val loanCount: String? // 대출건수 (지역 또는 세부지역 기준으로 조회했을 경우)
            )
        }
    }
}
