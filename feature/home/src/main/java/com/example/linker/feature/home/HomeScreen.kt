package com.example.linker.feature.home

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapRoute(onLogout: ()->Unit, vm: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        vm.locations.collect { vm.onLocation(it) }
    }


    MapScreen(
        tracking = vm.tracking,
        polyline = vm.polyline,
        onStart = vm::onStart,
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
        onLogout = onLogout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    tracking: Boolean,
    polyline: List<GeoPoint>,
    onStart: ()->Unit,
    onStop: ()->Unit,
    onExport: ()->Unit,
    onLogout: ()->Unit,
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    Scaffold(topBar = { TopAppBar(title = { Text("نقشه") }, actions = { TextButton(onClick = onLogout) { Text("خروج") } }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p)) {
            AndroidView(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setMultiTouchControls(true)
                        controller.setZoom(16.0)
                        overlays.clear()
                        mapView = this
                    }
                },
                update = { view ->
                    if (polyline.isNotEmpty()) {
                        val line = Polyline().apply {
                            setPoints(polyline)
                        }
                        view.overlays.removeAll { it is Polyline }
                        view.overlays.add(line)
                        view.controller.setCenter(polyline.last())
                        view.invalidate()
                    }
                }
            )


            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (!tracking) Button(onClick = onStart, modifier = Modifier.weight(1f)) { Text("شروع") } else {
                    Button(onClick = onStop, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors()) { Text("پایان") }
                }
                if (!tracking && polyline.isNotEmpty()) OutlinedButton(onClick = onExport, modifier = Modifier.weight(1f)) { Text("دانلود خروجی") }
            }
        }
    }
}