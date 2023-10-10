package com.nomargin.cynema.data.local.entity

data class CommentModel(
    val body: String = "",
    val isSpoiler: Boolean = false,
    val postId: String = "",
)