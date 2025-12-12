package com.sultonuzdev.pft.data.media

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.sultonuzdev.pft.R

class PomodoroTimerMediaController(
    private val context: Context
) {
    private val mediaPlayer = MediaPlayer.create(context, R.raw.start_music)


    fun playSound() {
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

    fun vibrateDevice() {
        val vibrator = getVibratorService(context)

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
}