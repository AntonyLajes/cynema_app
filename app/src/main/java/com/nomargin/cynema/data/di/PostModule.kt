package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.repository.PostRepository
import com.nomargin.cynema.data.repository.PostRepositoryImpl
import com.nomargin.cynema.data.usecase.PostUseCase
import com.nomargin.cynema.data.usecase.PostUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PostModule {

    @Binds
    abstract fun bindsPostRepositoryImpl(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    abstract fun bindsPostUseCaseImpl(
        postUseCaseImpl: PostUseCaseImpl
    ): PostUseCase

}