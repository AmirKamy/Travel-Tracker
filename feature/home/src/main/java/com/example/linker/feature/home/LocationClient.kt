package com.example.linker.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import android.location.Location as AndroidLocation

interface LocationClient {
    fun locationUpdates(): Flow<AndroidLocation>
}

@Singleton
class FusedLocationClient @Inject constructor(
    @ApplicationContext private val ctx: Context
) : LocationClient {

    @SuppressLint("MissingPermission")
    override fun locationUpdates(): Flow<AndroidLocation> = callbackFlow<AndroidLocation> {
        val client = LocationServices.getFusedLocationProviderClient(ctx)

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2_000L                 // هر ۲ ثانیه
        ).setMinUpdateIntervalMillis(1_000L) // اختیاری
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                trySend(loc)
                // .onFailure { /* log if needed */ }
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callback) }
    }
}
