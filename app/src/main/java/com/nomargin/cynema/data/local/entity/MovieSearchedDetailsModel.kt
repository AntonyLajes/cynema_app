package com.nomargin.cynema.data.local.entity

import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel

data class MovieSearchedDetailsModel(
    val id: Int = 0,
    val title: String? = "",
    val backgroundPath: String? = "",
    val posterPath: String? = "",
    val releaseDate: String? = "",
    val genres: List<GenreModel> = listOf()
)
