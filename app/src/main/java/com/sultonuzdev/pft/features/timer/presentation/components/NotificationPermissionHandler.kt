package com.sultonuzdev.pft.features.timer.presentation.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.util.Log

/**
 * Composable that handles notification permission request for Android 13+
 */
@Composable
fun NotificationPermissionHandler() {
    var permissionRequested by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Notification permission is only required on Android 13+ (API 33+)
    val permissionNeeded = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

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