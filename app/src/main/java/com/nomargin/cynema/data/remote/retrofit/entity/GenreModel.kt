package com.nomargin.cynema.data.remote.retrofit.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.nomargin.cynema.util.Constants

@Entity(tableName = Constants.LOCAL_STORAGE.genreTableName)
data class GenreModel (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = Constants.LOCAL_STORAGE.genreIdColumnName)
    @SerializedName("id")
    val genreId: Int,
    @ColumnInfo(name = Constants.LOCAL_STORAGE.genreDescColumnName)
    @SerializedName("name")
    val genreDesc: String
)