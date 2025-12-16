package com.sultonuzdev.pft.presentation.timer.contract

import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.core.ui.utils.UiEffect
import com.sultonuzdev.pft.core.ui.utils.UiIntent
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import java.time.LocalDate

object TimerMviContract {

    data class TimerUiState(
        // Timer state
        val settings: PomodoroTimerSettings = PomodoroTimerSettings(),
        val currentType: TimerType = TimerType.POMODORO,
        val timerState: TimerState = TimerState.IDLE,
        val totalTimeMillis: Long = 25 * 60 * 1000L,
        val remainingTimeMillis: Long = 25 * 60 * 1000L,
        val formattedTime: String = "25:00",
        val progressFraction: Float = 1.0f,
        val currentTimeMillis: Long = 0L,

        // Current session tracking
        val currentSessionPomodoros: Int = 0,

        // Today's statistics
        val todayStats: DailyStats = DailyStats(
            date = LocalDate.now(),
            completedPomodoros = 0,
            totalFocusMinutes = 0
        ),

        val timerStyle: TimerStyle = TimerStyle.REGULAR,

        // Loading state
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    /**
     * Represents user actions for the Timer screen
     */
    sealed interface TimerIntent : UiIntent {
        data object StartTimer : TimerIntent
        data object PauseTimer : TimerIntent
        data object ResumeTimer : TimerIntent
        data object FinishTimer : TimerIntent
        data object SkipTimer : TimerIntent
        data class SetTimerStyle(val timerStyle: TimerStyle) : TimerIntent

        data object NavigateToSettings : TimerIntent
        data object NavigateToStats : TimerIntent
        data object NavigateToTimerStyle : TimerIntent




    }

    sealed interface TimerEffect : UiEffect {
        data class ShowMessage(val message: String) : TimerEffect
        data class ShowQuote(val quote: String) : TimerEffect

        data object NavigateToSettings : TimerEffect
        data object NavigateToStats : TimerEffect
        data object NavigateToTimerStyle : TimerEffect

    }
}