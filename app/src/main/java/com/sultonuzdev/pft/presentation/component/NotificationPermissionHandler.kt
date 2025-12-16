package com.sultonuzdev.pft.presentation.component

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Composable that handles notification permission request for Android 13+
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationPermissionHandler() {
    var permissionRequested by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Notification permission is only required on Android 13+ (API 33+)
    val permissionNeeded = true

    // Create permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("NotificationPermission", "Permission granted: $isGranted")
        permissionRequested = true
    }

    // Request permission if needed and not already requested
    LaunchedEffect(Unit) {
        if (permissionNeeded && !permissionRequested) {
            val permissionState = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permissionState != PackageManager.PERMISSION_GRANTED) {
                Log.d("NotificationPermission", "Requesting notification permission")
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                Log.d("NotificationPermission", "Notification permission already granted")
            }
        }
    }
}