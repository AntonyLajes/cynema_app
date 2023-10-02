package com.nomargin.cynema.data.local

data class PostModel(
    val title: String = "",
    val body: String = "",
    val isSpoiler: Boolean = false,
    val movieId: String = "",
)