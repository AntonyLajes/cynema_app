package com.nomargin.cynema_app.data.di

import com.nomargin.cynema_app.data.remote.firebase.signin.FirebaseAuthUseCase
import com.nomargin.cynema_app.data.remote.firebase.signin.FirebaseAuthUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
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