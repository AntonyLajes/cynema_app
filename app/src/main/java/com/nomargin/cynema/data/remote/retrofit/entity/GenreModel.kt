package com.nomargin.cynema.data.remote.retrofit.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.nomargin.cynema.util.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.LOCAL_STORAGE.genreTableName)
@Parcelize
data class GenreModel (
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = Constants.LOCAL_STORAGE.genreIdColumnName)
    val genreId: Int,
    @SerializedName("name")
    @ColumnInfo(name = Constants.LOCAL_STORAGE.genreDescColumnName)
    val genreDesc: String
) : Parcelable