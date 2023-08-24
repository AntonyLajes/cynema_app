package com.nomargin.cynema_app.data.di

import com.nomargin.cynema_app.data.repository.AuthenticationRepository
import com.nomargin.cynema_app.data.repository.AuthenticationRepositoryImpl
import com.nomargin.cynema_app.data.usecase.ValidateAttributesUseCase
import com.nomargin.cynema_app.data.usecase.ValidateAttributesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthenticationModule {

    @Binds
    abstract fun bindsSignUp(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindsValidateAttribute(
        validateAttributesUseCaseImpl: ValidateAttributesUseCaseImpl
    ): ValidateAttributesUseCase

}