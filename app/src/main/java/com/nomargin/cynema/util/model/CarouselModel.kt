package com.nomargin.cynema.util.model

import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel

data class CarouselModel (
    val id: Int,
    val title: String,
    val backgroundPath: String,
    val posterPath: String,
    val genres: List<GenreModel>?
)