
package com.linker.core.domain.irepository

import com.example.linker.core.model.TrackPoint

interface TrackingRepository {
suspend fun startTrack(): Result<Long> // returns trackId
suspend fun addPoint(trackId: Long, lat: Double, lon: Double)
suspend fun endTrack(trackId: Long)
suspend fun getTrackPoints(trackId: Long): List<TrackPoint>
}