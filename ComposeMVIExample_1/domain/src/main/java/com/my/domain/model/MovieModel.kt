package com.my.domain.model

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieModelResults: List<MovieModelResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)