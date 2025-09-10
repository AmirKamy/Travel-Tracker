package com.example.linker.feature.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.linker.core.designsystem.component.MapBottomBar
import com.example.linker.core.designsystem.component.rememberLocationPermissionRequester
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

private const val TAG = "MapOverlay"
@Composable
fun MapRoute(onLogout: () -> Unit, vm: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        vm.locations.collect { vm.onLocation(it) }
    }


    MapScreen(
        tracking = vm.tracking,
        polyline = vm.polyline,
        onStart = {vm.onStart()},
        onStop = vm::onStop,
        onExport = {
            scope.launch {
                val uri = vm.exportCsv(context)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "دانلود/اشتراک CSV"))
            }
        },
        onLogout = onLogout,
        startFakeRoutes = { vm.startFakeRouteFromFirstPoint(context) }
    )
}

private fun hasLocationPermission(context: Context): Boolean {
    val fine = androidx.core.content.ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    val coarse = androidx.core.content.ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    return fine || coarse
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    tracking: Boolean,
    polyline: List<GeoPoint>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onExport: () -> Unit,
    onLogout: () -> Unit,
    startFakeRoutes: () -> Unit,
) {
    val context = LocalContext.current
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var trackLine by remember { mutableStateOf<Polyline?>(null) }
    var myLocationOverlay by remember { mutableStateOf<MyLocationNewOverlay?>(null) }

    var hasLocationPerm by remember { mutableStateOf(hasLocationPermission(context)) }
    var afterGrant by remember { mutableStateOf<(() -> Unit)?>(null) }


    val requestLocationPerms = rememberLocationPermissionRequester(
        onGranted = {
            hasLocationPerm = true
            afterGrant?.invoke()
            afterGrant = null
        },
        onDenied = {
            Toast.makeText(context, "برای ادامه دسترسی به موقعیت لازم است", Toast.LENGTH_SHORT).show()
        }
    )
    fun ensurePermThen(action: () -> Unit) {
        if (hasLocationPerm) action() else { afterGrant = action; requestLocationPerms() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("نقشه") },
                actions = { TextButton(onClick = onLogout) { Text("خروج") } })
        },
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(bottom = 140.dp),onClick = {
                ensurePermThen {
                    ensureLocationEnabled(context) {
                        centerOnCurrentLocation(mapView, myLocationOverlay)
                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.my_location),
                    contentDescription = "موقعیت من",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
        ) {
            AndroidView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setMultiTouchControls(true)
                        controller.setZoom(16.0)
                        overlays.clear()
                        mapView = this
                    }
                },
                update = { view ->
                    if (trackLine == null) {
                        trackLine = Polyline().apply {
                            outlinePaint.apply {
                                color = Color.parseColor("#2F80ED")
                                strokeWidth = 12f
                                isAntiAlias = true
                                strokeCap = Paint.Cap.ROUND
                                strokeJoin = Paint.Join.ROUND
                            }
                        }
                        view.overlays.add(trackLine)
                    }
                    if (polyline.isNotEmpty()) {
                        trackLine?.setPoints(polyline)
                        view.controller.animateTo(polyline.last(), 17.0, 800L)
                    }
                    view.invalidate()
                }
            )

            DisposableEffect(mapView) {
                if (mapView != null) {
                    val provider = FusedMyLocationProvider(mapView!!.context)
                    val overlay = MyLocationNewOverlay(provider, mapView).apply {
                        enableMyLocation()
                        enableFollowLocation()
                    }
                    mapView!!.overlays.add(overlay)
                    myLocationOverlay = overlay
                }

                onDispose {
                    myLocationOverlay?.disableMyLocation()
                    mapView?.overlays?.remove(myLocationOverlay)
                    myLocationOverlay = null
                }
            }

            MapBottomBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .imePadding(),
                tracking = tracking,
                canExport = !tracking && polyline.isNotEmpty(),
                onStart = {
                    ensurePermThen { onStart.invoke() }
                },
                onStop = onStop,
                onExport = onExport,
                polyline = polyline,
                // if we are in debug mode we can add fake points for QA test
                // first must add your app to "mock location app" :
                // adb shell appops set com.example.traveltracker android:mock_location allow
                startFakeRoutes = if (BuildConfig.DEBUG) startFakeRoutes else null
            )
        }
    }
}

private fun centerOnCurrentLocation(
    mapView: MapView?,
    overlay: MyLocationNewOverlay?,
    zoom: Double = 17.0
) {
    if (mapView == null || overlay == null) return
    val loc = overlay.myLocation
    if (loc != null) {
        val p = GeoPoint(loc.latitude, loc.longitude)
        mapView.controller.animateTo(p, zoom, 800L)
    } else {
        overlay.runOnFirstFix {
            val last = overlay.myLocation ?: return@runOnFirstFix
            val p = GeoPoint(last.latitude, last.longitude)
            mapView.post {
                mapView.controller.animateTo(p, zoom, 800L)
            }
        }
        overlay.enableMyLocation()
    }
}


fun ensureLocationEnabled(context: Context, onReady: () -> Unit) {
    val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L).build()
    val settingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(request)
        .setAlwaysShow(true)
        .build()

    val client = LocationServices.getSettingsClient(context)
    client.checkLocationSettings(settingsRequest)
        .addOnSuccessListener { onReady() }
        .addOnFailureListener { ex ->
            if (ex is ResolvableApiException) {
                try {
                    ex.startResolutionForResult((context as Activity), 1001)
                } catch (_: Exception) { /* no-op */
                }
            } else {
                Toast.makeText(context, "امکان دسترسی به موقعیت شما نیست", Toast.LENGTH_SHORT)
                    .show()
            }
        }
}