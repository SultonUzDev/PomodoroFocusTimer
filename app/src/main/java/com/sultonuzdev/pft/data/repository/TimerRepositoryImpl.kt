package com.sultonuzdev.pft.data.repository

import android.os.SystemClock
import android.util.Log
import com.sultonuzdev.pft.core.util.Constants.MILLIS_IN_SECOND
import com.sultonuzdev.pft.core.util.Constants.PAUSE_LOOP_DELAY_MILLIS
import com.sultonuzdev.pft.core.util.Constants.TIMER_COMPLETION_DELAY_MILLIS
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.core.util.formatToMinutesAndSeconds
import com.sultonuzdev.pft.data.db.dao.PomodoroDao
import com.sultonuzdev.pft.data.mapper.toEntity
import com.sultonuzdev.pft.domain.model.NewTimerState
import com.sultonuzdev.pft.domain.model.Pomodoro
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.repository.SettingsRepository
import com.sultonuzdev.pft.domain.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TimerRepositoryImpl"

/**
 * Modern timer repository implementation
 * Handles all timer business logic separate from Android service layer
 */
@Singleton
class TimerRepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao,
    private val settingsRepository: SettingsRepository,
) : TimerRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null
    private var settingsJob: Job? = null

    // Timer tracking using SystemClock for reliability
    private var timerStartElapsedTime: Long = 0L
    private var totalPausedDuration: Long = 0L  // Total time spent paused
    private var pauseStartTime: Long = 0L       // When pause button was pressed
    private var sessionStartTime: LocalDate? = null

    // Current settings cache
    private var currentSettings: PomodoroTimerSettings = PomodoroTimerSettings()

    // Completed sessions counter
    private var completedPomodoros: Int = 0
    private var totalSessions: Int = 0

    private val _timerState = MutableStateFlow(NewTimerState())
    override val timerState: StateFlow<NewTimerState> = _timerState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        settingsJob = repositoryScope.launch {
            try {
                settingsRepository.getDefaultSettings().collectLatest { settings ->
                    Log.d(TAG, "Settings updated: $settings")
                    currentSettings = settings

                    // Update timer state with new settings
                    _timerState.value = _timerState.value.copy(settings = settings)

                    // Update timer durations if idle
                    if (_timerState.value.timerState == TimerState.IDLE) {
                        updateTimerDurationsFromSettings()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error observing settings", e)
            }
        }
    }

    private fun updateTimerDurationsFromSettings() {
        val duration = getDurationForTimerType(_timerState.value.currentType)
        _timerState.value = _timerState.value.copy(
            totalTimeMillis = duration,
            remainingTimeMillis = duration,
            formattedTime = duration.formatToMinutesAndSeconds()
        )
        Log.d(TAG, "Updated timer duration to ${duration}ms for ${_timerState.value.currentType}")
    }

    private fun getDurationForTimerType(timerType: TimerType): Long {
        return when (timerType) {
            TimerType.POMODORO -> currentSettings.pomodoroMinutes * 60 * 1000L
            TimerType.SHORT_BREAK -> currentSettings.shortBreakMinutes * 60 * 1000L
            TimerType.LONG_BREAK -> currentSettings.longBreakMinutes * 60 * 1000L
        }
    }

    override suspend fun startTimer() {
        Log.d(TAG, "startTimer: type=${_timerState.value.currentType}")

        // Cancel any existing timer
        timerJob?.cancel()

        val currentType = _timerState.value.currentType
        val duration = getDurationForTimerType(currentType)

        // Update state
        _timerState.value = _timerState.value.copy(
            timerState = TimerState.RUNNING,
            totalTimeMillis = duration,
            remainingTimeMillis = duration,
            formattedTime = duration.formatToMinutesAndSeconds(),
            startedAt = LocalDate.now()
        )

        sessionStartTime = LocalDate.now()

        // Start the countdown
        startTimerCoroutine()
    }

    private fun startTimerCoroutine() {
        timerStartElapsedTime = SystemClock.elapsedRealtime()
        totalPausedDuration = 0L
        pauseStartTime = 0L


        timerJob = repositoryScope.launch {
            try {
                while (_timerState.value.remainingTimeMillis > 0) {
                    // Check if we should continue (not paused or stopped)
                    if (_timerState.value.timerState != TimerState.RUNNING) {
                        Log.d(TAG, "Timer coroutine paused/stopped, waiting...")
                        delay(PAUSE_LOOP_DELAY_MILLIS)
                        continue
                    }

                    delay(MILLIS_IN_SECOND)

                    // Only update if still running
                    if (_timerState.value.timerState == TimerState.RUNNING) {
                        val currentElapsedTime = SystemClock.elapsedRealtime()
                        // Calculate actual elapsed time excluding paused periods
                        val actualElapsedTime =
                            currentElapsedTime - timerStartElapsedTime - totalPausedDuration
                        val newRemainingTime =
                            (_timerState.value.totalTimeMillis - actualElapsedTime).coerceAtLeast(0)

                        // Update state
                        _timerState.value = _timerState.value.copy(
                            remainingTimeMillis = newRemainingTime,
                            formattedTime = newRemainingTime.formatToMinutesAndSeconds(),
                            currentTimeMillis = System.currentTimeMillis()
                        )

                        // Check if timer completed
                        if (newRemainingTime <= 0) {
                            timerCompleted()
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in timer coroutine", e)
            }
        }
    }

    private suspend fun timerCompleted() {
        Log.d(TAG, "timerCompleted - Timer naturally finished")

        // DON'T cancel the job here - we're running inside it!
        // Let it complete naturally after this function finishes

        val currentType = _timerState.value.currentType
        val sessionStart = sessionStartTime

        Log.d(TAG, "Session info: type=$currentType, startTime=$sessionStart")

        // Capture values before state reset
        val capturedTotal = _timerState.value.totalTimeMillis
        val capturedRemaining = _timerState.value.remainingTimeMillis

        Log.d(TAG, "Time tracking: total=${capturedTotal}ms, remaining=${capturedRemaining}ms")

        // Save to database
        if (sessionStart != null) {
            try {
                val focusedMillis = capturedTotal - capturedRemaining
                val focusedSeconds = (focusedMillis / 1000).coerceAtLeast(0)
                val plannedSeconds = when (currentType) {
                    TimerType.POMODORO -> (currentSettings.pomodoroMinutes * 60).toLong()
                    TimerType.SHORT_BREAK -> (currentSettings.shortBreakMinutes * 60).toLong()
                    TimerType.LONG_BREAK -> (currentSettings.longBreakMinutes * 60).toLong()
                }

                Log.d(
                    TAG,
                    "Saving completed $currentType: planned=${plannedSeconds}s, focused=${focusedSeconds}s, isCompleted=true"
                )

                val pomodoro = Pomodoro(
                    id = 0,
                    timerType = currentType,
                    plannedDurationSeconds = plannedSeconds,
                    focusedDurationSeconds = focusedSeconds,
                    isCompleted = true,
                    startedAt = sessionStart
                )
                savePomodoro(pomodoro)
                Log.d(TAG, "✓ Successfully saved $currentType session to database")
            } catch (e: Exception) {
                Log.e(TAG, "✗ Failed to save completed session", e)
            }
        } else {
            Log.w(TAG, "⚠ Session start time is null, skipping database save")
        }

        // Update state to completed
        _timerState.value = _timerState.value.copy(
            timerState = TimerState.COMPLETED,
            remainingTimeMillis = 0,
            formattedTime = "00:00"
        )

        // Update counters if Pomodoro
        if (currentType == TimerType.POMODORO) {
            completedPomodoros += 1
            val newSessionPomodoros = _timerState.value.currentSessionPomodoros + 1
            _timerState.value = _timerState.value.copy(
                currentSessionPomodoros = newSessionPomodoros
            )
            Log.d(
                TAG,
                "Pomodoro completed - Total: $completedPomodoros, Session: $newSessionPomodoros"
            )
        }

        sessionStartTime = null

        // Auto-transition after delay
        delay(TIMER_COMPLETION_DELAY_MILLIS)
        val nextType = getNextTimerType()
        Log.d(TAG, "Auto-transitioning from $currentType to $nextType")
        changeTimerType(nextType)
    }

    private fun getNextTimerType(): TimerType {
        val currentType = _timerState.value.currentType
        val sessionPomodoros = _timerState.value.currentSessionPomodoros
        val pomodorosBeforeLongBreak = currentSettings.pomodoroCycleLength

        Log.d(
            TAG,
            "getNextTimerType: currentType=$currentType, sessionPomodoros=$sessionPomodoros, cycleLength=$pomodorosBeforeLongBreak"
        )

        return when (currentType) {
            TimerType.POMODORO -> {
                if (sessionPomodoros >= pomodorosBeforeLongBreak) {
                    Log.d(
                        TAG,
                        "Session complete ($sessionPomodoros >= $pomodorosBeforeLongBreak) -> LONG_BREAK"
                    )
                    TimerType.LONG_BREAK
                } else {
                    Log.d(
                        TAG,
                        "Session continues ($sessionPomodoros < $pomodorosBeforeLongBreak) -> SHORT_BREAK"
                    )
                    TimerType.SHORT_BREAK
                }
            }

            TimerType.SHORT_BREAK -> {
                Log.d(TAG, "Short break over -> POMODORO")
                TimerType.POMODORO
            }

            TimerType.LONG_BREAK -> {
                Log.d(TAG, "Long break over -> Resetting session, starting new cycle")
                // Reset session
                _timerState.value = _timerState.value.copy(currentSessionPomodoros = 0)
                totalSessions += 1
                TimerType.POMODORO
            }
        }
    }

    private fun changeTimerType(type: TimerType) {
        Log.d(TAG, "changeTimerType to $type")

        // Don't cancel here - let the timer job complete naturally
        // Job is already stopped by state change or completion

        val newDuration = getDurationForTimerType(type)

        _timerState.value = _timerState.value.copy(
            currentType = type,
            timerState = TimerState.IDLE,
            totalTimeMillis = newDuration,
            remainingTimeMillis = newDuration,
            formattedTime = newDuration.formatToMinutesAndSeconds()
        )

        sessionStartTime = null
    }

    override suspend fun pauseTimer() {
        Log.d(TAG, "pauseTimer")
        if (_timerState.value.timerState == TimerState.RUNNING) {
            // Record when pause started
            pauseStartTime = SystemClock.elapsedRealtime()

            _timerState.value = _timerState.value.copy(timerState = TimerState.PAUSED)

            Log.d(TAG, "Timer paused at ${_timerState.value.formattedTime}")
        }
    }

    override suspend fun resumeTimer() {
        Log.d(TAG, "resumeTimer")
        if (_timerState.value.timerState == TimerState.PAUSED) {
            // Calculate how long we were paused and add to total paused duration
            val resumeTime = SystemClock.elapsedRealtime()
            val pauseDuration = resumeTime - pauseStartTime
            totalPausedDuration += pauseDuration

            // IMPORTANT: Keep original totalTimeMillis for correct progress calculation
            // Only change state to RUNNING, don't modify totalTimeMillis or timerStartElapsedTime
            _timerState.value = _timerState.value.copy(
                timerState = TimerState.RUNNING
                // totalTimeMillis stays the same - it's the ORIGINAL duration
            )

            Log.d(
                TAG,
                "Timer resumed at ${_timerState.value.formattedTime}, paused for ${pauseDuration}ms"
            )
        }
    }

    override suspend fun finishTimer() {
        Log.d(TAG, "finishTimer - user stopped early")

        // Cancel the timer loop (safe here because finishTimer runs in ViewModel scope)
        timerJob?.cancel()
        timerJob = null

        val currentType = _timerState.value.currentType
        val sessionStart = sessionStartTime

        // Capture values before state reset
        val capturedTotal = _timerState.value.totalTimeMillis
        val capturedRemaining = _timerState.value.remainingTimeMillis

        // Save to database (marked as incomplete)
        if (sessionStart != null) {
            try {
                val focusedMillis = capturedTotal - capturedRemaining
                val focusedSeconds = (focusedMillis / 1000).coerceAtLeast(0)
                val plannedSeconds = when (currentType) {
                    TimerType.POMODORO -> (currentSettings.pomodoroMinutes * 60).toLong()
                    TimerType.SHORT_BREAK -> (currentSettings.shortBreakMinutes * 60).toLong()
                    TimerType.LONG_BREAK -> (currentSettings.longBreakMinutes * 60).toLong()
                }

                val pomodoro = Pomodoro(
                    id = 0,
                    timerType = currentType,
                    plannedDurationSeconds = plannedSeconds,
                    focusedDurationSeconds = focusedSeconds,
                    isCompleted = false,
                    startedAt = sessionStart
                )
                savePomodoro(pomodoro)
                Log.d(
                    TAG,
                    "Stopped $currentType session saved as incomplete (${focusedSeconds}s/${plannedSeconds}s)"
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save stopped session", e)
            }
        }


        sessionStartTime = null

        // Auto-transition after delay
        val nextType = getNextTimerType()
        changeTimerType(nextType)

        // Update state
        _timerState.value = _timerState.value.copy(
            timerState = TimerState.COMPLETED,
            remainingTimeMillis = 0,
            formattedTime = getDurationForTimerType(nextType).formatToMinutesAndSeconds()
        )

    }

    override suspend fun skipTimer() {
        Log.d(TAG, "skipTimer - no save")

        timerJob?.cancel()
        timerJob = null

        // Don't save to database


        sessionStartTime = null

        // Auto-transition after delay
        delay(TIMER_COMPLETION_DELAY_MILLIS)
        val nextType = getNextTimerType()
        changeTimerType(nextType)

        _timerState.value = _timerState.value.copy(
            timerState = TimerState.IDLE,
            remainingTimeMillis = 0,
            formattedTime = getDurationForTimerType(nextType).formatToMinutesAndSeconds()

        )
    }

    override suspend fun completedTimer() {
        _timerState.value = _timerState.value.copy(timerState = TimerState.COMPLETED)
    }

    override suspend fun savePomodoro(pomodoro: Pomodoro) {
        try {
            Log.d(
                TAG,
                "savePomodoro: Inserting ${pomodoro.timerType} - planned=${pomodoro.plannedDurationSeconds}s, focused=${pomodoro.focusedDurationSeconds}s, completed=${pomodoro.isCompleted}"
            )
            pomodoroDao.insertSession(pomodoro.toEntity())
            Log.d(TAG, "savePomodoro: ✓ Insert completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "savePomodoro: ✗ Insert failed", e)
            throw e // Re-throw to be caught by caller
        }
    }

    fun cleanup() {
        timerJob?.cancel()
        settingsJob?.cancel()
    }
}