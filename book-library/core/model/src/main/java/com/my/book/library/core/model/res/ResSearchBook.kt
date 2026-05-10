package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

data class ResSearchBook(
    @SerializedName("response")
    val response: ResponseData
) {
    data class ResponseData(
        @SerializedName("request")
        val request: RequestData,
        @SerializedName("numFound")
        val numFound: Int,
        @SerializedName("docs")
        val docs: List<BookWrapper>
    ) {
        data class RequestData(
            @SerializedName("keyword")
            val keyword: String?,
            @SerializedName("sort")
            val sort: String?,
            @SerializedName("direction")
            val direction: String?,
            @SerializedName("exactMatch")
            val exactMatch: Boolean?,
            @SerializedName("pageNo")
            val pageNo: Int,
            @SerializedName("pageSize")
            val pageSize: Int,
            @SerializedName("format")
            val format: String?
        )

        data class BookWrapper(
            @SerializedName("doc")
            val doc: BookInfo
        ) {
            data class BookInfo(
                @SerializedName("bookname")
                val bookName: String?,
                @SerializedName("authors")
                val authors: String?,
                @SerializedName("publisher")
                val publisher: String?,
                @SerializedName("publication_year")
                val publicationYear: String?,
                @SerializedName("isbn13")
                val isbn13: String?,
                @SerializedName("addition_symbol")
                val additionSymbol: String?,
                @SerializedName("vol")
                val vol: String?,
                @SerializedName("class_no")
                val classNo: String?,
                @SerializedName("class_nm")
                val classNm: String?,
                @SerializedName("bookImageURL")
                val bookImageUrl: String?,
                @SerializedName("bookDtlUrl")
                val bookDtlUrl: String?,
                @SerializedName("loan_count")
                val loanCount: String?
            )
        }
    }
}
