package com.example.linker.feature.home.di

import com.example.linker.feature.home.FusedLocationClient
import com.example.linker.feature.home.LocationClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    @Singleton
    abstract fun bindLocationClient(impl: FusedLocationClient): LocationClient
}

