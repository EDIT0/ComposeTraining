package com.my.book.library.core.model.res

import com.google.gson.annotations.SerializedName

data class ResCheckBookAvailability(
    @SerializedName("response")
    val response: ResponseData
) {
    data class ResponseData(
        @SerializedName("request")
        val request: RequestData,
        @SerializedName("result")
        val result: ResultData
    ) {
        data class RequestData(
            @SerializedName("isbn13")
            val isbn13: String?,
            @SerializedName("libCode")
            val libCode: String?,
            @SerializedName("format")
            val format: String?
        )

        data class ResultData(
            @SerializedName("hasBook")
            val hasBook: String?,
            @SerializedName("loanAvailable")
            val loanAvailable: String?
        )
    }
}
