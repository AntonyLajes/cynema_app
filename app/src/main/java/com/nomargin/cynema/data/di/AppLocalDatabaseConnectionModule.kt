package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.repository.AppLocalDatabaseRepository
import com.nomargin.cynema.data.repository.AppLocalDatabaseRepositoryImpl
import com.nomargin.cynema.data.usecase.AppLocalDatabaseUseCase
import com.nomargin.cynema.data.usecase.AppLocalDatabaseUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppLocalDatabaseConnectionModule {

    @Binds
    abstract fun bindAppLocalDatabaseRepositoryImpl(
        appLocalDatabaseRepositoryImpl: AppLocalDatabaseRepositoryImpl
    ): AppLocalDatabaseRepository

    @Binds
    abstract fun bindAppLocalDatabaseUseCaseImpl(
        appLocalDatabaseUseCaseImpl: AppLocalDatabaseUseCaseImpl
    ): AppLocalDatabaseUseCase

}