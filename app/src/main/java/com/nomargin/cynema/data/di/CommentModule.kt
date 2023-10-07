package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.repository.CommentRepository
import com.nomargin.cynema.data.repository.CommentRepositoryImpl
import com.nomargin.cynema.data.usecase.CommentUseCase
import com.nomargin.cynema.data.usecase.CommentUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CommentModule {

    @Binds
    abstract fun bindsCommentRepositoryImpl(
        commentRepositoryImpl: CommentRepositoryImpl,
    ): CommentRepository

    @Binds
    abstract fun bindsCommentUseCaseImpl(
        commentUseCaseImpl: CommentUseCaseImpl,
    ): CommentUseCase

}