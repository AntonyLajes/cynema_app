package com.nomargin.cynema.data.remote.retrofit.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.nomargin.cynema.util.Constants

@Entity(tableName = Constants.LOCAL_DATABASE.genreTableName)
data class GenreModel (
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.LOCAL_DATABASE.genreIdColumnName)
    val genreId: Int?,
    @SerializedName("name")
    @ColumnInfo(name = Constants.LOCAL_DATABASE.genreDescColumnName)
    val genreDesc: String?
)