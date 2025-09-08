package com.example.linker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.linker.core.database.model.TrackPointEntity

@Dao
interface TrackPointDao {
@Insert
suspend fun insert(point: TrackPointEntity): Long
@Query("SELECT * FROM track_points WHERE trackId = :trackId ORDER BY timestamp ASC")
suspend fun getForTrack(trackId: Long): List<TrackPointEntity>
}