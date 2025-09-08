package com.example.core.data.di

import com.example.core.data.SessionManager
import com.example.core.data.datasource.AuthLocalDataSource
import com.example.core.data.datasource.TrackingLocalDataSource
import com.example.core.data.repository.AuthRepositoryImpl
import com.example.core.data.repository.TrackingRepositoryImpl
import com.linker.core.domain.irepository.AuthRepository
import com.linker.core.domain.irepository.TrackingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideAuthRepository(ds: AuthLocalDataSource, session: SessionManager): AuthRepository =
        AuthRepositoryImpl(ds, session)
    @Provides
    @Singleton
    fun provideTrackingRepository(ds: TrackingLocalDataSource): TrackingRepository =
        TrackingRepositoryImpl(ds)
}