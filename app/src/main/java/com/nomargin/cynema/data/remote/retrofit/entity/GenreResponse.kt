package com.nomargin.cynema.data.remote.retrofit.entity

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    val genreList: List<GenreModel>
)
