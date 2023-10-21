package com.nomargin.cynema.data.local.entity

import com.google.firebase.auth.FirebaseUser
import com.nomargin.cynema.data.remote.firebase.entity.PostDatabaseModel
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel

data class CommentAppearanceModel(
    val commentId: String = "",
    val user: UserProfileDataModel?,
    val post: PostDatabaseModel?,
    val body: String = "",
    val isSpoiler: Boolean? = null,
    var timestamp: String = "",
    val votes: String = "0",
    val answers: List<String> = arrayListOf(),
    val answersQuantity: String = "0",
    val usersWhoVoted: List<String> = arrayListOf(),
    val usersWhoUpVoted: List<String> = arrayListOf(),
    val usersWhoDownVoted: List<String> = arrayListOf(),
    val currentUser: FirebaseUser? = null,
    val active: Boolean = true
)