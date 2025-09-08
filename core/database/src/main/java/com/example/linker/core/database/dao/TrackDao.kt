package com.example.linker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.linker.core.database.model.TrackEntity

@Dao
interface TrackDao {
@Insert
suspend fun insert(track: TrackEntity): Long
@Query("UPDATE tracks SET endedAt = :end WHERE id = :id")
suspend fun endTrack(id: Long, end: Long)
@Query("SELECT * FROM tracks WHERE id = :id")
suspend fun getById(id: Long): TrackEntity?
}