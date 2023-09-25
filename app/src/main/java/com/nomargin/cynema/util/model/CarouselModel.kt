package com.nomargin.cynema.util.model

import android.os.Parcelable
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarouselModel (
    val id: Int,
    val title: String,
    val backgroundPath: String,
    val posterPath: String,
    val genres: List<GenreModel>
): Parcelable