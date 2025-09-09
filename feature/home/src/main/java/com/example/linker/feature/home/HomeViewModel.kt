package com.example.linker.feature.home

import android.content.Context
import android.location.Location
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linker.core.domain.authusecase.LogoutUseCase
import com.linker.core.domain.trackingusecase.AddPointUseCase
import com.linker.core.domain.trackingusecase.EndTrackUseCase
import com.linker.core.domain.trackingusecase.GetTrackPointsUseCase
import com.linker.core.domain.trackingusecase.StartTrackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val startTrack: StartTrackUseCase,
    private val addPoint: AddPointUseCase,
    private val endTrack: EndTrackUseCase,
    private val getPoints: GetTrackPointsUseCase,
    private val logout: LogoutUseCase,
    private val location: LocationClient
) : ViewModel() {
    var tracking by mutableStateOf(false); private set
    var currentTrackId by mutableStateOf<Long?>(null); private set
    var polyline = mutableStateListOf<GeoPoint>()

    val locations: Flow<Location> = location.locationUpdates()

    fun onStart() {
        viewModelScope.launch {
            val id = startTrack().getOrThrow()
            currentTrackId = id
            tracking = true
        }
    }


    fun onStop() {
        viewModelScope.launch {
            currentTrackId?.let { endTrack(it) }
            tracking = false
        }
    }


    fun onLogout() { viewModelScope.launch { logout() } }


    fun onLocation(l: Location) {
        if (!tracking) return
        viewModelScope.launch {
            currentTrackId?.let { addPoint(it, l.latitude, l.longitude) }
            polyline.add(GeoPoint(l.latitude, l.longitude))
        }
    }


    suspend fun exportCsv(ctx: Context): Uri {
        val trackId = currentTrackId ?: error("No track")
        val pts = getPoints(trackId)
        val file = File(ctx.cacheDir, "track_${trackId}.csv")
        FileWriter(file).use { w ->
            w.appendLine("lat,lon,timestamp")
            pts.forEach { w.appendLine("${it.lat},${it.lon},${it.timestamp}") }
        }
        return FileProvider.getUriForFile(ctx, ctx.packageName + ".provider", file)
    }

    // for test
    fun startFakeRoute(context: Context) {
        val route = mutableListOf<Pair<Double, Double>>()
        val baseLat = 35.700000
        val baseLon = 51.400000

        for (i in 0 until 25) {
            val lat = baseLat + i * 0.0002
            val lon = baseLon + i * 0.0002
            route.add(lat to lon)
        }

        context.pushFakeRoute(route, intervalMs = 800)
    }
}