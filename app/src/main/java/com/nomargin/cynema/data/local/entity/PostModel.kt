package com.nomargin.cynema.data.local.entity

data class PostModel(
    val title: String = "",
    val body: String = "",
    val isSpoiler: Boolean = false,
    val movieId: String = "",
)