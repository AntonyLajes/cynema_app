package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.repository.TheMovieDatabaseRepository
import com.nomargin.cynema.data.repository.TheMovieDatabaseRepositoryImpl
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCase
import com.nomargin.cynema.data.usecase.TheMovieDatabaseApiUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TheMovieDatabaseApiModule {

    @Binds
    abstract fun bindTheMovieDatabaseRepositoryImpl(
        theMovieDatabaseRepositoryImpl: TheMovieDatabaseRepositoryImpl
    ): TheMovieDatabaseRepository

    @Binds
    abstract fun bindTheMovieDatabaseApiUseCaseImpl(
        theMovieDatabaseApiUseCaseImpl: TheMovieDatabaseApiUseCaseImpl
    ): TheMovieDatabaseApiUseCase

}