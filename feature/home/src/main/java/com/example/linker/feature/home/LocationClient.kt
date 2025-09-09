package com.example.linker.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import android.location.Location as AndroidLocation

interface LocationClient {
    fun locationUpdates(): Flow<AndroidLocation>

    suspend fun currentOnce(timeoutMs: Long = 10_000): android.location.Location?
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


    @SuppressLint("MissingPermission")
    override suspend fun currentOnce(timeoutMs: Long): android.location.Location? =
        withTimeoutOrNull(timeoutMs) {
            suspendCancellableCoroutine { cont ->
                val client = LocationServices.getFusedLocationProviderClient(ctx)
                val token = CancellationTokenSource()
                client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
                    .addOnSuccessListener { cont.resume(it) }
                    .addOnFailureListener { cont.resume(null) }
                cont.invokeOnCancellation { token.cancel() }
            }
        }

}
