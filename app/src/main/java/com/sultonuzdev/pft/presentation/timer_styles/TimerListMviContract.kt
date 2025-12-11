package com.sultonuzdev.pft.presentation.timer_styles

import com.sultonuzdev.pft.core.enums.TimerStyle

object TimerListMviContract {

    data class TimerListState(
        val timerList: List<TimerOption> =
            listOf(
                TimerOption(
                    style = TimerStyle.REGULAR,
                    icon = "🍅",
                    title = "Regular",
                    description = "Minimalist dark theme optimized for distraction-free studying. Large timer display with landscape layout for maximum focus.",
                    features = listOf("Minimalistic", "Large Timer"),
                ),
                TimerOption(
                    style = TimerStyle.READING,
                    icon = "📖",
                    title = "Reading Session",
                    description = "Warm, cozy theme inspired by vintage books. Serif typography and sepia tones create a comfortable reading atmosphere.",
                    features = listOf("Warm Colors", "Book Design"),
                ),
                TimerOption(
                    style = TimerStyle.MEDITATION,
                    icon = "🧘",
                    title = "Meditation",
                    description = "Calm, peaceful theme with breathing animations. Minimal interface helps you stay present and focused on your practice.",
                    features = listOf("Breathing Circle", "Calming"),
                ),
                TimerOption(
                    style = TimerStyle.CODING,
                    icon = "💻",
                    title = "Coding Session",
                    description = "Terminal-inspired theme with monospace fonts and syntax highlighting. Feels like your favorite IDE for focused coding.",
                    features = listOf("Terminal Style", "Dev Tools"),
                )
            ),
        val selectedStyle: TimerStyle = TimerStyle.REGULAR,
    )

    sealed interface TimerListEffect {
        data object NavigateUp : TimerListEffect
        data class ShowMessage(val message: String) : TimerListEffect

    }

    sealed interface TimerListIntent {
        object LoadTimerList : TimerListIntent
        data class SelectStyle(val timerStyle: TimerStyle) : TimerListIntent
        data object NavigateBack : TimerListIntent
    }
}

data class TimerOption(
    val style: TimerStyle,
    val icon: String,
    val title: String,
    val description: String,
    val features: List<String>,
)