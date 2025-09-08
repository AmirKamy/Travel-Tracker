package com.example.core.data.datasource

import com.example.linker.core.database.dao.TrackDao
import com.example.linker.core.database.dao.TrackPointDao
import com.example.linker.core.database.model.TrackEntity
import com.example.linker.core.database.model.TrackPointEntity
import javax.inject.Inject

class TrackingLocalDataSource @Inject constructor(
    private val trackDao: TrackDao,
    private val pointDao: TrackPointDao,
) {
    suspend fun start(): Long = trackDao.insert(TrackEntity(startedAt = System.currentTimeMillis()))
    suspend fun end(id: Long) = trackDao.endTrack(id, System.currentTimeMillis())
    suspend fun addPoint(entity: TrackPointEntity) = pointDao.insert(entity)
    suspend fun getPoints(trackId: Long) = pointDao.getForTrack(trackId)
}