package com.nomargin.cynema.data.remote.retrofit.entity

import com.google.gson.annotations.SerializedName

data class LanguageModel(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val languageCode: String,
    @SerializedName("name")
    val languageName: String
)
