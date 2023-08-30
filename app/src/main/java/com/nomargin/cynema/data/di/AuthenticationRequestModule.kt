package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.remote.google.AuthenticationRequestUseCase
import com.nomargin.cynema.data.remote.google.AuthenticationRequestUseCaseImpl
import com.nomargin.cynema.data.repository.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationRequestModule {

    @Binds
    abstract fun bindsAAuthenticationRequest(
        authenticationRequestImpl: AuthenticationRequestUseCaseImpl
    ): AuthenticationRequestUseCase

}