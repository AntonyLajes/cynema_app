package com.nomargin.cynema.data.local.entity

data class MovieSearchedAppearanceModel(
    val pageNumber: Int,
    val results: List<MovieSearchedDetailsModel>,
    val totalPages: Int,
    val totalResults: Int,
)