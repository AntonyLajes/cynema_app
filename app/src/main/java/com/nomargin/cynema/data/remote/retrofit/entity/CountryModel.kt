package com.nomargin.cynema.data.remote.retrofit.entity

import com.google.gson.annotations.SerializedName

data class CountryModel(
    @SerializedName("iso_3166_1")
    val countryCode: String,
    @SerializedName("name")
    val countryName: String
)