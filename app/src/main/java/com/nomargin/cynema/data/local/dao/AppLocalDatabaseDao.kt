package com.nomargin.cynema.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nomargin.cynema.data.local.entity.Genre

@Dao
interface AppLocalDatabaseDao {

    @Insert
    suspend fun insertGenres(genders: List<Genre>)

    @Query("SELECT * FROM genre")
    suspend fun selectAllGenres(): List<Genre>

    @Query("SELECT * FROM genre WHERE id = :genreId")
    suspend fun selectGenreById(genreId: Int): List<Genre>

}