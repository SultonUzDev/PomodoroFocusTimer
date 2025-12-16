# NewTimerViewModel Usage Guide

## Overview

`NewTimerViewModel` is a modern, clean implementation that uses `TimerRepository` directly without needing any service manager. It follows the MVI pattern using the existing `TimerMviContract`.

## Key Features

✅ **Direct Repository Access** - No service manager boilerplate
✅ **Uses Existing MVI Contract** - No changes to `TimerMviContract.kt`
✅ **Single Source of Truth** - Repository is shared between ViewModel and Service
✅ **Automatic State Mapping** - Converts `NewTimerState` to `TimerUiState`
✅ **Full Feature Parity** - All features from old TimerViewModel
✅ **Cleaner Architecture** - Less code, simpler flow

## Architecture Flow

```
User Action → TimerIntent → NewTimerViewModel → TimerRepository
                                    ↓
                           NewTimerService (for notifications)
                                    ↓
                           Repository StateFlow
                                    ↓
                           ViewModel observes & maps to UI state
                                    ↓
                           UI updates
```

## Quick Start

### 1. Add NewTimerService to AndroidManifest.xml

```xml
<service
    android:name=".presentation.service.NewTimerService"
    android:enabled="true"
    android:exported="false"
    android:foregroundServiceType="dataSync" />
```

### 2. Use in Composable

```kotlin
@Composable
fun NewTimerScreen(
    viewModel: NewTimerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TimerMviContract.TimerEffect.ShowMessage -> {
                    // Show toast or snackbar
                }
                is TimerMviContract.TimerEffect.ShowQuote -> {
                    // Show motivational quote dialog
                }
                // ... handle other effects
            }
        }
    }

    // UI
    Column {
        Text("Timer: ${uiState.formattedTime}")
        Text("Type: ${uiState.currentType}")
        Text("State: ${uiState.timerState}")

        // Buttons
        when (uiState.timerState) {
            TimerState.IDLE -> {
                Button(onClick = {
                    viewModel.processIntent(TimerMviContract.TimerIntent.StartTimer)
                }) {
                    Text("Start")
                }
            }
            TimerState.RUNNING -> {
                Button(onClick = {
                    viewModel.processIntent(TimerMviContract.TimerIntent.PauseTimer)
                }) {
                    Text("Pause")
                }
                Button(onClick = {
                    viewModel.processIntent(TimerMviContract.TimerIntent.FinishTimer)
                }) {
                    Text("Finish")
                }
                Button(onClick = {
                    viewModel.processIntent(TimerMviContract.TimerIntent.SkipTimer)
                }) {
                    Text("Skip")
                }
            }
            TimerState.PAUSED -> {
                Button(onClick = {
                    viewModel.processIntent(TimerMviContract.TimerIntent.ResumeTimer)
                }) {
                    Text("Resume")
                }
            }
            TimerState.COMPLETED -> {
                Text("Timer completed!")
            }
        }
    }
}
```

## Available UI State Fields

The `TimerUiState` includes:

```kotlin
data class TimerUiState(
    // Timer state
    val settings: PomodoroTimerSettings,
    val currentType: TimerType,              // POMODORO, SHORT_BREAK, LONG_BREAK
    val timerState: TimerState,              // IDLE, RUNNING, PAUSED, COMPLETED
    val totalTimeMillis: Long,               // Total timer duration
    val remainingTimeMillis: Long,           // Time remaining
    val formattedTime: String,               // "25:00" format
    val progressFraction: Float,             // 0.0 to 1.0
    val currentTimeMillis: Long,             // Current timestamp

    // Session tracking
    val currentSessionPomodoros: Int,        // Pomodoros in current session

    // Statistics
    val todayStats: DailyStats,              // Today's completed pomodoros

    // UI settings
    val timerStyle: TimerStyle,              // REGULAR, CODING, READING, etc.
    val isLoading: Boolean,
    val errorMessage: String?
)
```

## Available Intents

```kotlin
sealed interface TimerIntent {
    data object StartTimer : TimerIntent
    data object PauseTimer : TimerIntent
    data object ResumeTimer : TimerIntent
    data object FinishTimer : TimerIntent      // Saves progress, marks incomplete
    data object SkipTimer : TimerIntent        // No save, just skip
    data class SetTimerStyle(val timerStyle: TimerStyle) : TimerIntent

    data object NavigateToSettings : TimerIntent
    data object NavigateToStats : TimerIntent
    data object NavigateToTimerStyle : TimerIntent
}
```

## Available Effects

```kotlin
sealed interface TimerEffect {
    data class ShowMessage(val message: String) : TimerEffect
    data class ShowQuote(val quote: String) : TimerEffect

    data object NavigateToSettings : TimerEffect
    data object NavigateToStats : TimerEffect
    data object NavigateToTimerStyle : TimerEffect
}
```

## Key Behaviors

### Timer Actions

1. **Start** - Starts timer with current settings, launches foreground service
2. **Pause** - Pauses timer, preserves remaining time
3. **Resume** - Continues from where it paused
4. **Finish** - Stops timer, saves focused time as incomplete, transitions to next
5. **Skip** - Jumps to next timer type without saving

### Auto-Transitions

- After Pomodoro completes → Short Break (or Long Break if cycle complete)
- After Short Break → Pomodoro
- After Long Break → Pomodoro (session resets)

### Notifications

The service automatically handles:
- Creating foreground notification when timer starts
- Updating notification every 5 seconds while running
- Showing completion notification
- Action buttons (Pause, Resume, Skip, Finish)

## Comparison with Old TimerViewModel

| Feature | Old TimerViewModel | NewTimerViewModel |
|---------|-------------------|-------------------|
| Service Manager | ✅ Required | ❌ Not needed |
| State Observation | Multiple flows | Single flow |
| State Mapping | Manual | Automatic |
| Boilerplate | High | Low |
| Testability | Medium | High |
| Architecture | Service-centric | Repository-centric |

## Benefits

1. **Simpler Code** - No service manager, direct repository access
2. **Single Source of Truth** - Repository state is THE state
3. **Better Testability** - Can test ViewModel with mock repository
4. **Cleaner Architecture** - Clear separation of concerns
5. **Same Contract** - Uses existing `TimerMviContract`, no UI changes needed
6. **Full Feature Parity** - Everything from old ViewModel works the same

## Migration from Old TimerViewModel

1. Replace `hiltViewModel<TimerViewModel>()` with `hiltViewModel<NewTimerViewModel>()`
2. That's it! The contract is the same, so no UI code changes needed

```kotlin
// Before
@Composable
fun TimerScreen(viewModel: TimerViewModel = hiltViewModel()) { }

// After
@Composable
fun TimerScreen(viewModel: NewTimerViewModel = hiltViewModel()) { }
```

## Notes

- ✅ Repository is `@Singleton` - shared between ViewModel and Service
- ✅ Service runs independently for notifications
- ✅ Both observe the same repository state flow
- ✅ No race conditions - repository handles timing correctly
- ✅ Database saves are accurate (total - remaining)
