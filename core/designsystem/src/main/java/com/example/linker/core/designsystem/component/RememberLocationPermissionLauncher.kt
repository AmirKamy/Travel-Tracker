package com.example.linker.core.designsystem.component

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberLocationPermissionRequester(
    onGranted: () -> Unit,
    onDenied: (() -> Unit)? = null
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        // اگر یکی از FINE/COARSE قبول شد، می‌تونیم باهاش کار کنیم
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fine || coarse) onGranted() else onDenied?.invoke()
    }

    return {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}

