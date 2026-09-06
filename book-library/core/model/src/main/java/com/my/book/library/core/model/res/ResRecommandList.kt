package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

/**
 * 마니아를 위한 추천도서 조회 응답
 */
data class ResRecommandList(
    @SerializedName("response")
    val response: ResponseData?
) {
    data class ResponseData(
        @SerializedName("resultNum")
        val resultNum: Int?, // 응답결과 건수
        @SerializedName("docs")
        val docs: List<BookWrapper>? // 목록
    ) {
        data class BookWrapper(
            @SerializedName("book")
            val book: BookData? // 도서
        ) {
            data class BookData(
                @SerializedName("no")
                val no: Int?, // 순번
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
                val bookImageURL: String? // 책표지 URL
            )
        }
    }
}
