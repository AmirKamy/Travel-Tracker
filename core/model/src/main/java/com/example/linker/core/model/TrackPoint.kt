package com.example.linker.core.model

data class TrackPoint(
val id: Long = 0,
val trackId: Long,
val lat: Double,
val lon: Double,
val timestamp: Long
)