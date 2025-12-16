package com.sultonuzdev.pft.presentation.timer_list

import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.domain.model.TimerOption

object TimerListMviContract {

    data class TimerListState(
        val timerList: List<TimerOption> = timerOptions,
        val selectedStyle: TimerStyle = TimerStyle.REGULAR,
    )

    sealed interface TimerListEffect {
        data class ShowMessage(val message: String) : TimerListEffect
        data object NavigateBack : TimerListEffect
    }

    sealed interface TimerListIntent {
        object LoadTimerList : TimerListIntent
        data class SetTimerStyleDefault(val timerStyle: TimerStyle) : TimerListIntent
        data object NavigateBack : TimerListIntent
    }

}

val timerOptions = listOf(
    TimerOption(
        style = TimerStyle.REGULAR,
        icon = "🍅",
        title = "Regular",
        description = "Minimalist dark theme optimized for distraction-free studying. Large timer display with landscape layout for maximum focus.",
        features = listOf("Minimal", "Circle"),
    ),
    TimerOption(
        style = TimerStyle.STUDY,
        icon = "📚",
        title = "Study Focus",
        description = "Minimalist dark theme optimized for distraction-free studying. Large timer display with landscape layout for maximum focus.",
        features = listOf("Focused", "Large"),
    ),
    TimerOption(
        style = TimerStyle.READING,
        icon = "📖",
        title = "Reading Session",
        description = "Warm, cozy theme inspired by vintage books. Serif typography and sepia tones create a comfortable reading atmosphere.",
        features = listOf("Warm", "Vintage"),
    ),
    TimerOption(
        style = TimerStyle.MEDITATION,
        icon = "🧘",
        title = "Meditation",
        description = "Calm, peaceful theme with breathing animations. Minimal interface helps you stay present and focused on your practice.",
        features = listOf("Breathing", "Calm"),
    ),
    TimerOption(
        style = TimerStyle.CODING,
        icon = "💻",
        title = "Coding Session",
        description = "Terminal-inspired theme with monospace fonts and syntax highlighting. Feels like your favorite IDE for focused coding.",
        features = listOf("Terminal", "Monospace"),
    )
)


