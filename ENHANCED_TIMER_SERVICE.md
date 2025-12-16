# Enhanced Timer Service Documentation

## Overview

The `EnhancedTimerService` is a modern, Clean Architecture-based foreground service for managing Pomodoro timers. It uses **Repository Pattern + StateFlow** for state management, providing a single source of truth for timer state across the entire app.

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│  UI Layer (Compose Screens)                        │
│  - RegularTimerScreen                              │
│  - StudyTimerScreen                                │
│  - CodingTimerScreen, etc.                         │
└──────────────────┬──────────────────────────────────┘
                   │ observes
┌──────────────────▼──────────────────────────────────┐
│  ViewModel                                          │
│  - Observes timerState: StateFlow<TimerState2>    │
│  - Sends actions via repository.sendAction()       │
└──────────────────┬──────────────────────────────────┘
                   │ observes & sends actions
┌──────────────────▼──────────────────────────────────┐
│  TimerRepository (SINGLE SOURCE OF TRUTH)          │
│  - timerState: StateFlow<TimerState2>             │
│  - timerActions: SharedFlow<TimerAction>          │
│  - updateTimerState(state)                         │
│  - sendAction(action)                              │
└─────────┬───────────────────────▲───────────────────┘
          │ updates               │ observes actions
          │                       │
┌─────────▼───────────────────────┴───────────────────┐
│  EnhancedTimerService                              │
│  - Observes actions from repository                │
│  - Updates state in repository                     │
│  - Coroutine-based timer with SystemClock          │
│  - Database integration                            │
│  - Notification management                         │
└─────────────────────┬───────────────────────────────┘
                      │
         ┌────────────┴────────────┐
         │                         │
┌────────▼──────────┐    ┌────────▼──────────────┐
│  Notification     │    │  TimerActionReceiver  │
│  - Live updates   │    │  - Handles button     │
│  - Action buttons │    │    clicks from        │
│                   │    │    notification       │
└───────────────────┘    └───────────────────────┘
```

---

## Key Features

### ✅ **Single Source of Truth**
- All timer state lives in `TimerRepository`
- Service and UI both observe the same `StateFlow<TimerState2>`
- No state synchronization issues

### ✅ **Bidirectional Communication**
- **UI → Service**: Send `TimerAction` via repository
- **Service → UI**: Observe `TimerState2` from repository

### ✅ **Reliable Timing**
- Uses `SystemClock.elapsedRealtime()` (survives device sleep)
- Coroutine-based timer loop (flexible, non-blocking)
- Proper pause/resume tracking

### ✅ **Pomodoro Features**
- Multiple timer types (Pomodoro, Short Break, Long Break)
- Auto-transitions between types
- Session & cycle tracking
- Database integration for completed sessions

### ✅ **Production Ready**
- Foreground service with notifications
- Battery-optimized notification updates
- Settings integration
- Error handling & logging

---

## How to Use

### 1. **In Your ViewModel**

```kotlin
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val application: Application
) : ViewModel() {

    // Observe timer state from repository
    val timerState: StateFlow<TimerState2> = timerRepository.timerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimerState2.idle()
        )

    // Send actions to repository
    fun startTimer(timerType: TimerType, durationMillis: Long) {
        viewModelScope.launch {
            // Start the service
            startService()

            // Send action
            timerRepository.sendAction(
                TimerAction.Start(timerType, durationMillis)
            )
        }
    }

    fun pauseTimer() {
        viewModelScope.launch {
            timerRepository.sendAction(TimerAction.Pause)
        }
    }

    fun resumeTimer() {
        viewModelScope.launch {
            timerRepository.sendAction(TimerAction.Resume)
        }
    }

    fun stopTimer() {
        viewModelScope.launch {
            timerRepository.sendAction(TimerAction.Stop)
        }
    }

    fun skipTimer() {
        viewModelScope.launch {
            timerRepository.sendAction(TimerAction.Skip)
        }
    }

    private fun startService() {
        val intent = Intent(application, EnhancedTimerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.startForegroundService(intent)
        } else {
            application.startService(intent)
        }
    }
}
```

### 2. **In Your Composable UI**

```kotlin
@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    // Collect state
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display timer
        Text(
            text = timerState.formattedTime,
            style = MaterialTheme.typography.displayLarge
        )

        // Progress indicator
        CircularProgressIndicator(
            progress = timerState.progressFraction,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Timer controls based on state
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            when (timerState.status) {
                TimerState2.Status.IDLE -> {
                    Button(onClick = {
                        viewModel.startTimer(
                            TimerType.POMODORO,
                            25 * 60 * 1000L
                        )
                    }) {
                        Text("Start")
                    }
                }

                TimerState2.Status.RUNNING -> {
                    Button(onClick = { viewModel.pauseTimer() }) {
                        Text("Pause")
                    }
                    Button(onClick = { viewModel.skipTimer() }) {
                        Text("Skip")
                    }
                    Button(onClick = { viewModel.stopTimer() }) {
                        Text("Stop")
                    }
                }

                TimerState2.Status.PAUSED -> {
                    Button(onClick = { viewModel.resumeTimer() }) {
                        Text("Resume")
                    }
                    Button(onClick = { viewModel.stopTimer() }) {
                        Text("Stop")
                    }
                }

                TimerState2.Status.COMPLETED -> {
                    Text("Timer completed!")
                    Button(onClick = {
                        viewModel.startTimer(
                            TimerType.POMODORO,
                            25 * 60 * 1000L
                        )
                    }) {
                        Text("Restart")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Session info
        Text("Completed Pomodoros: ${timerState.completedPomodoros}")
        Text("Current Session: ${timerState.currentSessionPomodoros}")
    }
}
```

### 3. **Register in AndroidManifest.xml**

```xml
<manifest>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />

    <application>
        <!-- Enhanced Timer Service -->
        <service
            android:name=".presentation.service.EnhancedTimerService"
            android:enabled="true"
            android:exported="false"
            android:foregroundServiceType="specialUse">
            <property
                android:name="android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE"
                android:value="Pomodoro timer that requires precise timing" />
        </service>

        <!-- Broadcast receiver for notification actions -->
        <receiver
            android:name=".presentation.service.TimerActionReceiver"
            android:enabled="true"
            android:exported="false" />
    </application>
</manifest>
```

---

## State Model

### TimerState2

```kotlin
data class TimerState2(
    val status: Status = Status.IDLE,
    val timerType: TimerType = TimerType.POMODORO,
    val remainingTimeMillis: Long = 0L,
    val totalTimeMillis: Long = 0L,
    val progressFraction: Float = 1.0f,
    val formattedTime: String = "25:00",
    val completedPomodoros: Int = 0,
    val currentSessionPomodoros: Int = 0,
    val totalSessions: Int = 0
) {
    sealed class Status {
        data object IDLE : Status()
        data object RUNNING : Status()
        data object PAUSED : Status()
        data object COMPLETED : Status()
    }
}
```

### TimerAction

```kotlin
sealed class TimerAction : Serializable {
    data class Start(
        val timerType: TimerType,
        val durationMillis: Long
    ) : TimerAction()

    data object Pause : TimerAction()
    data object Resume : TimerAction()
    data object Stop : TimerAction()
    data object Skip : TimerAction()
    data class ChangeTimerType(val timerType: TimerType) : TimerAction()
}
```

---

## Pomodoro Cycle Logic

The service automatically handles Pomodoro cycles:

1. **Start**: Pomodoro timer (25 min)
2. **Complete**: Short break (5 min)
3. **Repeat** steps 1-2 until 4 pomodoros completed
4. **After 4 pomodoros**: Long break (15 min)
5. **After long break**: Reset session, start new cycle

This is all handled automatically by the service.

---

## Database Integration

Every timer session (completed or skipped) is saved to the database:

```kotlin
addPomodoro(
    type = TimerType.POMODORO,
    plannedDurationSeconds = 25 * 60,
    completed = true,  // or false if skipped
    startedTime = LocalDate.now(),
    focusedDurationSeconds = actualFocusedSeconds
)
```

This allows for:
- Statistics tracking
- History viewing
- Progress analytics

---

## Notification Management

The service shows a persistent notification with:
- Current timer state
- Remaining time
- Action buttons (Pause/Resume/Stop/Skip)

Notifications are throttled to update every 5 seconds to save battery.

---

## Migration from Old Service

If you're migrating from `TimerService.kt`:

### Replace this pattern:
```kotlin
// OLD: Direct service binding
private var timerService: TimerService? = null

serviceBinder.bind { service ->
    timerService = service
    timerService?.timerState?.collect { ... }
}

timerService?.startTimer(...)
```

### With this pattern:
```kotlin
// NEW: Repository-based
private val timerRepository: TimerRepository

timerRepository.timerState.collect { ... }

timerRepository.sendAction(TimerAction.Start(...))
```

---

## Benefits Over Old Approach

| Aspect | Old Service | Enhanced Service |
|--------|-------------|------------------|
| **State Management** | StateFlows in service | StateFlows in repository |
| **Communication** | Service binding | Repository actions |
| **Sync** | Manual binding/unbinding | Automatic via StateFlow |
| **Testing** | Hard (requires service) | Easy (mock repository) |
| **Architecture** | Service-centric | Repository-centric (Clean) |
| **Lifecycle** | Complex binding logic | Simple observation |
| **Process Death** | State loss risk | Repository survives |

---

## Summary

The `EnhancedTimerService` provides:

- ✅ **Clean Architecture** - Repository pattern with single source of truth
- ✅ **Reliable Timing** - SystemClock + Coroutines
- ✅ **Pomodoro Features** - Auto-transitions, cycles, session tracking
- ✅ **Production Ready** - Notifications, database, settings integration
- ✅ **Easy Testing** - Repository can be mocked
- ✅ **Lifecycle Safe** - No binding/unbinding complexity

Perfect for building a professional Pomodoro timer app! 🍅⏱️
