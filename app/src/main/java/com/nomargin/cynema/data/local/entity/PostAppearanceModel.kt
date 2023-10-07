package com.nomargin.cynema.data.local.entity

import com.google.firebase.auth.FirebaseUser
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.data.remote.retrofit.entity.MovieDetailsModel

data class PostAppearanceModel(
    val postId: String = "",
    val user: UserProfileDataModel?,
    val movie: MovieDetailsModel?,
    val title: String = "",
    val body: String = "",
    val isSpoiler: Boolean? = null,
    var timestamp: String = "",
    val votes: String = "0",
    val comments: List<String> = arrayListOf(),
    val commentsQuantity: String = "0",
    val usersWhoVoted: List<String> = arrayListOf(),
    val usersWhoUpVoted: List<String> = arrayListOf(),
    val usersWhoDownVoted: List<String> = arrayListOf(),
    val currentUser: FirebaseUser? = null
)