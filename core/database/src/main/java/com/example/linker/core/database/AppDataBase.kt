package com.example.linker.core.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.linker.core.database.dao.TrackDao
import com.example.linker.core.database.dao.TrackPointDao
import com.example.linker.core.database.dao.UserDao
import com.example.linker.core.database.model.TrackEntity
import com.example.linker.core.database.model.TrackPointEntity
import com.example.linker.core.database.model.UserEntity

@Database(entities = [UserEntity::class, TrackEntity::class, TrackPointEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun trackDao(): TrackDao
    abstract fun trackPointDao(): TrackPointDao
}