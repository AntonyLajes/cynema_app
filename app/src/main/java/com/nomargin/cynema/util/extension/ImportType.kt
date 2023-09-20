package com.nomargin.cynema.util.extension

import com.nomargin.cynema.data.local.entity.Genre
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel

fun GenreModel.toGenre(): Genre {
    return Genre(
        genreId = genreId,
        genreDesc = genreDesc
    )
}

fun Genre.toGenreModel(): GenreModel {
    return GenreModel(
        genreId = genreId,
        genreDesc = genreDesc
    )
}