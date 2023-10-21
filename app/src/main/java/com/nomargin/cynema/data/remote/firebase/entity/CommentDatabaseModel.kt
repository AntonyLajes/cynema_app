package com.nomargin.cynema.data.remote.firebase.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class CommentDatabaseModel(
    val id: String = "",
    val userId: String = "",
    val postId: String = "",
    val body: String = "",
    val isSpoiler: Boolean? = false,
    @ServerTimestamp
    var timestamp: Timestamp? = null,
    val usersWhoVoted: List<String> = arrayListOf(),
    val usersWhoUpVoted: List<String> = arrayListOf(),
    val usersWhoDownVoted: List<String> = arrayListOf(),
    val votes: Int = 0,
    val answers: List<String> = arrayListOf(),
    val answersQuantity: Int = 0,
    val isActive: Boolean = true
)