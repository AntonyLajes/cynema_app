package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.remote.firebase.signin.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.signin.FirebaseAuthUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {

    @Binds
    @Singleton
    abstract fun providesFirebaseAuth(
        firebaseAuthUseCase: FirebaseAuthUseCaseImpl
    ): FirebaseAuthUseCase

}