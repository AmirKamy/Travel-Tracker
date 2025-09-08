package com.linker.core.domain.trackingusecase

import com.linker.core.domain.irepository.TrackingRepository
import javax.inject.Inject

class StartTrackUseCase @Inject constructor(private val repo: TrackingRepository) {
suspend operator fun invoke() = repo.startTrack()
}
class AddPointUseCase @Inject constructor(private val repo: TrackingRepository) {
suspend operator fun invoke(trackId: Long, lat: Double, lon: Double) = repo.addPoint(trackId, lat, lon)
}
class EndTrackUseCase @Inject constructor(private val repo: TrackingRepository) {
suspend operator fun invoke(trackId: Long) = repo.endTrack(trackId)
}
class GetTrackPointsUseCase @Inject constructor(private val repo: TrackingRepository) {
suspend operator fun invoke(trackId: Long) = repo.getTrackPoints(trackId)
}