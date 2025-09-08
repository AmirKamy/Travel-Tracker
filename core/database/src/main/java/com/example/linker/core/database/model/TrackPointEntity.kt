package com.example.linker.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
tableName = "track_points",
foreignKeys = [ForeignKey(
    entity = TrackEntity::class,
    parentColumns = ["id"],
    childColumns = ["trackId"],
    onDelete = ForeignKey.CASCADE
)],
indices = [Index("trackId")]
)
data class TrackPointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trackId: Long,
    val lat: Double,
    val lon: Double,
    val timestamp: Long
)