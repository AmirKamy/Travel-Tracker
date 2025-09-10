package com.example.linker.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
fun Context.pushFakeRoute(points: List<Pair<Double, Double>>, intervalMs: Long = 1000L) {
    val fused = LocationServices.getFusedLocationProviderClient(this)
    CoroutineScope(Dispatchers.Default).launch {
        try {
            fused.setMockMode(true)
            for ((lat, lon) in points) {
                val loc = Location(LocationManager.GPS_PROVIDER).apply {
                    latitude = lat
                    longitude = lon
                    accuracy = 3f
                    time = System.currentTimeMillis()
                    elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
                    bearing = 0f
                    speed = 4f
                }
                fused.setMockLocation(loc)
                delay(intervalMs)
            }
        } finally {
            fused.setMockMode(false)
        }
    }
}
