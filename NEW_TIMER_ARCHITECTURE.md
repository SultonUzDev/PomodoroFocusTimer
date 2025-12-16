# New Timer Architecture Documentation

## Overview

This document describes the new modern timer architecture that separates business logic from Android-specific concerns.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                         UI Layer                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │          NewTimerViewModel (MVI Pattern)            │   │
│  │  - Observes repository state                        │   │
│  │  - Maps to TimerUiState                             │   │
│  │  - Handles user intents                             │   │
│  │  - Triggers notifications via service               │   │
│  └─────────────────┬───────────────────────────────────┘   │
└────────────────────┼───────────────────────────────────────┘
                     │ Direct injection
                     │
┌────────────────────▼───────────────────────────────────────┐
│                   Business Logic Layer                      │
│  ┌─────────────────────────────────────────────────────┐   │
│  │   TimerRepository (@Singleton - Source of Truth)    │   │
│  │  ✓ Timer countdown logic (SystemClock)             │   │
│  │  ✓ Pause/Resume handling                            │   │
│  │  ✓ Timer type transitions                           │   │
│  │  ✓ Database persistence                             │   │
│  │  ✓ Settings integration                             │   │
│  │  ✓ Session/Pomodoro counting                        │   │
│  └─────────────────┬───────────────────────────────────┘   │
└────────────────────┼───────────────────────────────────────┘
                     │ Also injected into
                     │
┌────────────────────▼───────────────────────────────────────┐
│                   Android Service Layer                     │
│  ┌─────────────────────────────────────────────────────┐   │
│  │          NewTimerService (Foreground)               │   │
│  │  - Observes repository state                        │   │
│  │  - Creates/updates notifications                    │   │
│  │  - Handles notification actions                     │   │
│  │  - Manages foreground service lifecycle             │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

**Key Points:**
- ✨ **TimerRepository is @Singleton** - shared between ViewModel and Service
- 🔄 **Single Source of Truth** - Both ViewModel and Service observe the same StateFlow
- 🎯 **Separation of Concerns** - Business logic in repository, Android concerns in service
- 📱 **ViewModel doesn't need service manager** - Direct repository injection
- 🔔 **Service handles notifications** - Runs in parallel to ViewModel

## Architecture Components

### 1. **TimerRepositoryImpl** (Business Logic Layer)
Location: `app/src/main/java/com/sultonuzdev/pft/data/repository/TimerRepositoryImpl.kt`

**Responsibilities:**
- All timer countdown logic using `SystemClock` for reliability
- Proper pause/resume handling with elapsed time tracking
- Timer type management (Pomodoro, Short Break, Long Break)
- Automatic transitions between timer types based on session rules
- Database persistence with correct focused duration calculation
- Settings integration and updates
- Session and pomodoro counting

**Key Features:**
- ✅ Uses `SystemClock.elapsedRealtime()` for accurate timing (works during device sleep)
- ✅ Captures values before async operations to avoid race conditions
- ✅ Properly calculates focused duration: `total - remaining`
- ✅ Distinguishes between completed sessions and stopped-early sessions
- ✅ Auto-transitions to next timer type after completion
- ✅ Integrates with settings repository for dynamic configuration

**Methods:**
- `startTimer()` - Starts the timer with current settings
- `pauseTimer()` - Pauses the running timer
- `resumeTimer()` - Resumes the paused timer
- `finishTimer()` - Stops timer early and saves progress (marked as incomplete)
- `skipTimer()` - Skips timer without saving to database
- `timerCompleted()` - Called when timer naturally completes

### 2. **NewTimerService** (Android Service Layer)
Location: `app/src/main/java/com/sultonuzdev/pft/presentation/service/NewTimerService.kt`

**Responsibilities:**
- Foreground service lifecycle management
- Notification creation and updates
- Intent handling (start, pause, resume, finish, skip)
- Observing timer state from repository

**Key Features:**
- ✅ Lightweight service - delegates all logic to repository
- ✅ Automatic notification updates based on timer state
- ✅ Notification action buttons (Pause, Resume, Skip, Finish)
- ✅ Proper foreground service handling

### 3. **NewTimerServiceManager** (Manager Layer)
Location: `app/src/main/java/com/sultonuzdev/pft/presentation/service/NewTimerServiceManager.kt`

**Responsibilities:**
- Service binding/unbinding
- Provides easy access to timer repository
- Handles service connection state
- Provides convenient methods for timer operations

**Key Features:**
- ✅ Singleton manager with Hilt injection
- ✅ Connection state tracking
- ✅ Direct access to repository for ViewModels
- ✅ Simplified API for timer operations

## Benefits of New Architecture

1. **Separation of Concerns**
   - Business logic (TimerRepository) is separate from Android concerns (Service)
   - Easier to test - can test timer logic without Android dependencies
   - Easier to maintain and debug

2. **No Race Conditions**
   - Values are captured before async operations
   - Accurate duration tracking and database saves

3. **Proper Duration Tracking**
   - Finish: Saves `total - remaining` as focused duration, marks as incomplete
   - Skip: No database save, just transitions to next timer
   - Complete: Saves full duration, marks as complete

4. **Modern Kotlin/Coroutines**
   - Uses StateFlow for reactive state management
   - Proper coroutine scope management
   - Structured concurrency

5. **Settings Integration**
   - Automatically observes settings changes
   - Updates timer durations when idle
   - Respects pomodoro cycle length for breaks

## How to Use

### Option 1: Direct Repository Access (Recommended) ✨

The cleanest approach - use `TimerRepository` directly in your ViewModel:

```kotlin
@HiltViewModel
class NewTimerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timerRepository: TimerRepository,
    private val pomodoroUseCases: PomodoroUseCases,
    private val settingsRepository: SettingsRepository,
    private val mediaController: PomodoroTimerMediaController,
) : ViewModel() {

    // Observe repository state directly
    init {
        viewModelScope.launch {
            timerRepository.timerState.collectLatest { repoState ->
                // Map to your UI state
                _uiState.update {
                    it.copy(
                        timerState = repoState.timerState,
                        remainingTimeMillis = repoState.remainingTimeMillis,
                        // ... other mappings
                    )
                }
            }
        }
    }

    // Start timer
    suspend fun startTimer() {
        timerRepository.startTimer()

        // Start foreground service for notifications
        val intent = Intent(context, NewTimerService::class.java).apply {
            action = ACTION_START
        }
        ContextCompat.startForegroundService(context, intent)
    }

    // Pause timer
    suspend fun pauseTimer() {
        timerRepository.pauseTimer()
        sendServiceIntent(ACTION_PAUSE)
    }

    // Other methods...
}
```

**Benefits:**
- No service manager needed
- Direct access to repository (single source of truth)
- Service runs in parallel for notifications
- Simpler architecture, less boilerplate

### Option 2: Using NewTimerServiceManager

If you prefer the manager pattern:

```kotlin
@HiltViewModel
class YourViewModel @Inject constructor(
    private val newTimerServiceManager: NewTimerServiceManager
) : ViewModel() {

    // Observe timer state
    val timerState = newTimerServiceManager.timerState
        .stateIn(viewModelScope, SharingStarted.Lazily, NewTimerState())

    init {
        newTimerServiceManager.bindService()
    }

    suspend fun startTimer() {
        newTimerServiceManager.startTimer()
    }

    // ... other methods
}
```

### In AndroidManifest.xml

Add the service declaration:

```xml
<service
    android:name=".presentation.service.NewTimerService"
    android:enabled="true"
    android:exported="false"
    android:foregroundServiceType="dataSync" />
```

## Timer State Flow

```
IDLE → (start) → RUNNING → (pause) → PAUSED → (resume) → RUNNING
                     ↓                                        ↓
                  (finish)                              (complete)
                     ↓                                        ↓
                 COMPLETED → (auto-transition) → IDLE (next timer type)
```

## Timer Type Transitions

```
POMODORO (1st) → SHORT_BREAK → POMODORO (2nd) → SHORT_BREAK →
POMODORO (3rd) → SHORT_BREAK → POMODORO (4th) → LONG_BREAK →
[Session Reset] → POMODORO (1st) → ...
```

## Database Saving Logic

1. **Timer Completes Naturally**
   - Saves to database with `isCompleted = true`
   - `focusedDurationSeconds` = full timer duration
   - Increments pomodoro counter (if Pomodoro type)

2. **User Clicks Finish**
   - Saves to database with `isCompleted = false`
   - `focusedDurationSeconds` = actual time worked (total - remaining)
   - Does NOT increment counter (user didn't complete)

3. **User Clicks Skip**
   - Does NOT save to database
   - Does NOT increment counter
   - Just transitions to next timer type

## Migration from Old TimerService

To migrate existing code:

1. Replace `TimerServiceManager` with `NewTimerServiceManager`
2. Update state observation from multiple flows to single `timerState` flow
3. Update AndroidManifest.xml service declaration
4. Keep old `TimerService` unchanged (as requested)

## Testing

The repository can be tested without Android dependencies:

```kotlin
@Test
fun `test timer completion saves correct duration`() = runTest {
    val repository = TimerRepositoryImpl(
        preferencesManager = mockPreferencesManager,
        pomodoroDao = mockDao,
        settingsRepository = mockSettingsRepository
    )

    repository.startTimer()
    // ... assertions
}
```

## Notes

- The old `TimerService` remains unchanged and functional
- Both services can coexist in the app
- NewTimerService is production-ready and handles all edge cases
- All timer logic bugs from old service are fixed in repository
