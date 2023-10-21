package com.nomargin.cynema.data.local.entity

data class CommentModel(
    val id: String? = null,
    val body: String = "",
    val isSpoiler: Boolean = false,
    val postId: String = "",
)