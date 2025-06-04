package com.sultonuzdev.pft.presentation.timer.components

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.presentation.timer.TimerViewModel
import com.sultonuzdev.pft.presentation.timer.utils.TimerEffect
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TimerEffectsHandler(
    viewModel: TimerViewModel,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current

    // Create and remember MediaPlayer to avoid recreating it on recomposition
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.start_music)
    }

    // Remember the vibrator service
    val vibrator = remember {
        getVibratorService(context)
    }

    // Clean up resources when leaving the composition
    DisposableEffect(Unit) {
        mediaPlayer?.setOnCompletionListener { mp -> mp.seekTo(0) }

        // Return a cleanup function that will be called when the composable leaves composition
        onDispose {
            mediaPlayer?.release()
        }
    }

    // Process UI effects
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is TimerEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                is TimerEffect.ShowQuote -> {
                    snackbarHostState.showSnackbar(effect.quote)
                }

                is TimerEffect.PlayTimerCompletedSound -> {
                    playTimerCompletedSound(mediaPlayer)
                }

                is TimerEffect.VibrateDevice -> {
                    vibrateDevice(vibrator)
                }
            }
        }
    }
}

// Play the timer completion sound
private fun playTimerCompletedSound(mediaPlayer: MediaPlayer?) {
    try {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.seekTo(0)
                it.start()
            }
        }
    } catch (e: Exception) {
        // Handle or log exception
        e.printStackTrace()
    }
}

// Vibrate the device
private fun vibrateDevice(vibrator: Vibrator?) {
    try {
        vibrator?.let {
            // For API 26 and above: VibrationEffect
            val effect = VibrationEffect.createOneShot(
                500, // Duration in milliseconds (500ms = 0.5 seconds)
                VibrationEffect.DEFAULT_AMPLITUDE
            )
            it.vibrate(effect)
        }
    } catch (e: Exception) {
        // Handle or log exception
        e.printStackTrace()
    }
}

// Helper function to get the Vibrator service based on API level
private fun getVibratorService(context: Context): Vibrator? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For API 31 and above: VibratorManager
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            // For API 30 and below: Vibrator
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    } catch (e: Exception) {
        // Handle exception (e.g., if the device doesn't have a vibrator)
        e.printStackTrace()
        null
    }
}