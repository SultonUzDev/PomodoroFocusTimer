package com.sultonuzdev.pft.features.timer.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sultonuzdev.pft.service.TimerServiceManager

/**
 * Handles lifecycle events for the timer, ensuring proper service binding
 */
@Composable
fun TimerLifecycleHandler(timerServiceManager: TimerServiceManager) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    // Bind to service when the composable enters foreground
                    timerServiceManager.bindService()
                }
                Lifecycle.Event.ON_STOP -> {
                    // Unbind from service when the composable enters background,
                    // but don't stop the service - it will continue running in the background
                    timerServiceManager.unbindService()
                }
                else -> { /* no-op */ }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}