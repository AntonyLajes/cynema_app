package com.nomargin.cynema.data.local.entity

data class PostModel(
    val id: String? = null,
    val title: String = "",
    val body: String = "",
    val isSpoiler: Boolean = false,
    val movieId: String = "",
)