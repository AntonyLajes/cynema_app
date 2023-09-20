package com.nomargin.cynema.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nomargin.cynema.util.Constants

@Entity(tableName = Constants.LOCAL_DATABASE.genreTableName)
data class Genre (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.LOCAL_DATABASE.genreIdColumnName)
    val genreId: Int?,
    @ColumnInfo(name = Constants.LOCAL_DATABASE.genreDescColumnName)
    val genreDesc: String?
)