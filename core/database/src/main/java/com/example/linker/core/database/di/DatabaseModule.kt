package com.example.linker.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.linker.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "mapapp.db").build()


    @Provides fun provideUserDao(db: AppDatabase) = db.userDao()
    @Provides fun provideTrackDao(db: AppDatabase) = db.trackDao()
    @Provides fun provideTrackPointDao(db: AppDatabase) = db.trackPointDao()
}