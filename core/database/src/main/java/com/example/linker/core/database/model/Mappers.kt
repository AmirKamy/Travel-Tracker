package com.example.linker.core.database.model

import com.example.linker.core.model.Track
import com.example.linker.core.model.TrackPoint
import com.example.linker.core.model.User

fun UserEntity.toModel() = User(id, firstName, lastName, age, birthDate, username, passwordHash)
fun User.toEntity() = UserEntity(id, firstName, lastName, age, birthDate, username, passwordHash)


fun TrackEntity.toModel() = Track(id, startedAt, endedAt)
fun Track.toEntity() = TrackEntity(id, startedAt, endedAt)


fun TrackPointEntity.toModel() = TrackPoint(id, trackId, lat, lon, timestamp)
fun TrackPoint.toEntity() = TrackPointEntity(id, trackId, lat, lon, timestamp)