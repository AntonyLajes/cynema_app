package com.nomargin.cynema.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel

@Dao
interface AppDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genres: List<GenreModel>)
    @Query("SELECT * FROM genre")
    suspend fun getAllGenres(): List<GenreModel>
    @Query("SELECT `desc` FROM genre WHERE id = :genreId")
    suspend fun getGenre(genreId: Int): GenreModel
}