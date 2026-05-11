package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

data class ResBookDetail(
    @SerializedName("response")
    val response: ResponseData
) {
    data class ResponseData(
        @SerializedName("request")
        val request: RequestData,
        @SerializedName("detail")
        val detail: List<BookWrapper>
    ) {
        data class RequestData(
            @SerializedName("isbn13")
            val isbn13: String?,
            @SerializedName("loaninfoYN")
            val loaninfoYN: String?,
            @SerializedName("format")
            val format: String?
        )

        data class BookWrapper(
            @SerializedName("book")
            val book: BookInfo
        ) {
            data class BookInfo(
                @SerializedName("no")
                val no: Int?,
                @SerializedName("bookname")
                val bookName: String?,
                @SerializedName("authors")
                val authors: String?,
                @SerializedName("publisher")
                val publisher: String?,
                @SerializedName("publication_date")
                val publicationDate: String?,
                @SerializedName("publication_year")
                val publicationYear: String?,
                @SerializedName("isbn")
                val isbn: String?,
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
                @SerializedName("description")
                val description: String?,
                @SerializedName("bookImageURL")
                val bookImageUrl: String?
            )
        }
    }
}
