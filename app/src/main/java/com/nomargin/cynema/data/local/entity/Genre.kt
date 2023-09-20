package com.nomargin.cynema.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nomargin.cynema.util.Constants

@Entity(tableName = Constants.LOCAL_STORAGE.genreTableName)
data class Genre(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = Constants.LOCAL_STORAGE.genreIdColumnName)
    val id: Int,
    @ColumnInfo(name = Constants.LOCAL_STORAGE.genreDescColumnName)
    val desc: String
)
