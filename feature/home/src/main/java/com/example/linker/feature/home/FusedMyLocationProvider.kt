package com.example.linker.feature.home

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class FusedMyLocationProvider(
    private val context: Context
) : IMyLocationProvider {

    private val fused = LocationServices.getFusedLocationProviderClient(context)
    private var callback: LocationCallback? = null
    private var last: Location? = null

    override fun startLocationProvider(consumer: IMyLocationConsumer?): Boolean {
        val req = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000L
        ).build()

        val cb = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                last = loc
                consumer?.onLocationChanged(loc, this@FusedMyLocationProvider)
            }
        }
        callback = cb
        fused.requestLocationUpdates(req, cb, Looper.getMainLooper())
        return true
    }

    override fun stopLocationProvider() {
        callback?.let { fused.removeLocationUpdates(it) }
        callback = null
    }

    override fun getLastKnownLocation(): Location? = last
    override fun destroy() = stopLocationProvider()
}
