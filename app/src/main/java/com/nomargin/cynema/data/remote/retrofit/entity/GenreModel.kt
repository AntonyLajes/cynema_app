package com.nomargin.cynema.data.remote.retrofit.entity

import com.google.gson.annotations.SerializedName

data class GenreModel (
    @SerializedName("id")
    val genreId: Int?,
    @SerializedName("name")
    val genreDesc: String?
)