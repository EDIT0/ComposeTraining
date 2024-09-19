package com.my.domain.model

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
    @SerializedName("results")
    var movieModelResults: List<MovieModelResult>? = null
)