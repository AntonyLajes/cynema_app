package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.repository.ProfileRepository
import com.nomargin.cynema.data.repository.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileModule {

    @Binds
    abstract fun providesProfileRepository(
        profileRepository: ProfileRepositoryImpl
    ): ProfileRepository

}