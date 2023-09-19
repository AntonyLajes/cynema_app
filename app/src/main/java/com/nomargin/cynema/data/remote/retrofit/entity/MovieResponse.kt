package com.nomargin.cynema.data.remote.retrofit.entity

import com.google.gson.annotations.SerializedName

data class MovieResponse (
    @SerializedName("page")
    val pageNumber: Int,
    @SerializedName("results")
    val results: List<MovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
)