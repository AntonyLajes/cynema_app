package com.nomargin.cynema.data.remote.firebase.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class PostDatabaseModel(
    val id: String = "",
    val userId: String = "",
    val movieId: String = "",
    val title: String = "",
    val body: String = "",
    val isSpoiler: Boolean? = null,
    @ServerTimestamp
    var timestamp: Timestamp? = null,
    val votes: Int = 0,
    val comments: List<String> = arrayListOf(),
    val commentsQuantity: Int = 0
)