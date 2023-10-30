package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.data.repository.AppLocalDatabaseRepository
import javax.inject.Inject

class AppLocalDatabaseUseCaseImpl @Inject constructor(
    private val appLocalDatabaseRepository: AppLocalDatabaseRepository
): AppLocalDatabaseUseCase {
    override suspend fun insertGenres(genres: List<GenreModel>) {
        appLocalDatabaseRepository.insertGenres(genres)
    }

    override suspend fun selectAllGenres(): List<GenreModel> {
        return appLocalDatabaseRepository.selectAllGenres()
    }

    override suspend fun selectGenreById(genreId: Int): GenreModel {
        return appLocalDatabaseRepository.selectGenreById(genreId)
    }

    override suspend fun closeDatabase() {
        appLocalDatabaseRepository.closeDatabase()
    }
}