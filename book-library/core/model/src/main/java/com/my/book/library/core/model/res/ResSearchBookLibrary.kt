package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

data class ResSearchBookLibrary(
    @SerializedName("response")
    val response: ResponseData
) {
    data class ResponseData(
        @SerializedName("request")
        val request: RequestData,
        @SerializedName("pageNo")
        val pageNo: Int,
        @SerializedName("pageSize")
        val pageSize: Int,
        @SerializedName("numFound")
        val numFound: Int,
        @SerializedName("resultNum")
        val resultNum: Int,
        @SerializedName("libs")
        val libs: List<LibraryWrapper>
    ) {
        data class RequestData(
            @SerializedName("pageNo")
            val pageNo: String,
            @SerializedName("pageSize")
            val pageSize: String
        )

        data class LibraryWrapper(
            @SerializedName("lib")
            val lib: LibraryInfo
        ) {
            data class LibraryInfo(
                @SerializedName("libCode")
                val libCode: String?,
                @SerializedName("libName")
                val libName: String?,
                @SerializedName("address")
                val address: String?,
                @SerializedName("tel")
                val tel: String?,
                @SerializedName("fax")
                val fax: String?,
                @SerializedName("latitude")
                val latitude: String?,
                @SerializedName("longitude")
                val longitude: String?,
                @SerializedName("homepage")
                val homepage: String?,
                @SerializedName("closed")
                val closed: String?,
                @SerializedName("operatingTime")
                val operatingTime: String?,
                @SerializedName("BookCount")
                val bookCount: String?
            )
        }
    }
}