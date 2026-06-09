package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

data class ResLibraryBookData(
    @SerializedName("response")
    val response: ResponseData
) {
    data class ResponseData(
        @SerializedName("request")
        val request: RequestData?,
        @SerializedName("libNm")
        val libNm: String?,
        @SerializedName("pageNo")
        val pageNo: Int?,
        @SerializedName("pageSize")
        val pageSize: Int?,
        @SerializedName("numFound")
        val numFound: Int?,
        @SerializedName("resultNum")
        val resultNum: Int?,
        @SerializedName("docs")
        val docs: List<DocWrapper>?
    ) {
        data class RequestData(
            @SerializedName("libCode")
            val libCode: String?,
            @SerializedName("type")
            val type: String?,
            @SerializedName("isbn13")
            val isbn13: String?,
            @SerializedName("format")
            val format: String?
        )

        data class DocWrapper(
            @SerializedName("doc")
            val doc: DocData?
        ) {
            data class DocData(
                @SerializedName("bookname")
                val bookname: String?,
                @SerializedName("authors")
                val authors: String?,
                @SerializedName("publisher")
                val publisher: String?,
                @SerializedName("publication_year")
                val publicationYear: String?,
                @SerializedName("isbn13")
                val isbn13: String?,
                @SerializedName("set_isbn13")
                val setIsbn13: String?,
                @SerializedName("bookImageURL")
                val bookImageURL: String?,
                @SerializedName("addition_symbol")
                val additionSymbol: String?,
                @SerializedName("vol")
                val vol: String?,
                @SerializedName("class_no")
                val classNo: String?,
                @SerializedName("class_nm")
                val classNm: String?,
                @SerializedName("callNumbers")
                val callNumbers: List<CallNumberWrapper>?,
                @SerializedName("reg_date")
                val regDate: String?
            )
        }

        data class CallNumberWrapper(
            @SerializedName("callNumber")
            val callNumber: CallNumberData?
        ) {
            data class CallNumberData(
                @SerializedName("separate_shelf_code")
                val separateShelfCode: String?,
                @SerializedName("separate_shelf_name")
                val separateShelfName: String?,
                @SerializedName("book_code")
                val bookCode: String?,
                @SerializedName("shelf_loc_code")
                val shelfLocCode: String?,
                @SerializedName("shelf_loc_name")
                val shelfLocName: String?,
                @SerializedName("copy_code")
                val copyCode: String?
            )
        }
    }
}
