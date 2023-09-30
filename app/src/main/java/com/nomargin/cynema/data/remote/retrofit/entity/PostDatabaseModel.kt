package com.nomargin.cynema.data.remote.retrofit.entity

import java.sql.Timestamp

data class PostDatabaseModel(
    val id: String,
    val userId: String,
    val movieId: String,
    val title: String,
    val body: String,
    val timestamp: Timestamp,
    val votes: Int,
    val comments: List<String>,
    val commentsQuantity: Int
)