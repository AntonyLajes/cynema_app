package com.nomargin.cynema.data.di

import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCase
import com.nomargin.cynema.data.remote.firebase.authentication.FirebaseAuthUseCaseImpl
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCase
import com.nomargin.cynema.data.remote.firebase.firestore.FirebaseFirestoreUseCaseImpl
import com.nomargin.cynema.data.remote.firebase.storage.FirebaseStorageUseCase
import com.nomargin.cynema.data.remote.firebase.storage.FirebaseStorageUseCaseImpl
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

    @Binds
    @Singleton
    abstract fun providesFirebaseFirestore(
        firebaseFirestoreUseCase: FirebaseFirestoreUseCaseImpl
    ): FirebaseFirestoreUseCase

    @Binds
    @Singleton
    abstract fun providesFirebaseStorage(
        firebaseStorageUseCase: FirebaseStorageUseCaseImpl
    ): FirebaseStorageUseCase
}