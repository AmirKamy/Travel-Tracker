package com.example.core.data.repository

import com.example.core.data.datasource.TrackingLocalDataSource
import com.example.linker.core.database.model.TrackPointEntity
import com.example.linker.core.database.model.toModel
import com.linker.core.domain.irepository.TrackingRepository
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val ds: TrackingLocalDataSource,
) : TrackingRepository {
    override suspend fun startTrack(): Result<Long> = runCatching { ds.start() }
    override suspend fun addPoint(trackId: Long, lat: Double, lon: Double) {
        ds.addPoint(
            TrackPointEntity(
                trackId = trackId,
                lat = lat,
                lon = lon,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun endTrack(trackId: Long) {
        ds.end(trackId)
    }

    override suspend fun getTrackPoints(trackId: Long) = ds.getPoints(trackId).map { it.toModel() }
}