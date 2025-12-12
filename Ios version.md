Pomodoro Focus Timer - iOS Development Guide

📱 PROJECT OVERVIEW

App Name: Pomodoro Focus Timer
Version: 1.1.0
Package: com.sultonuzdev.pft
Min SDK: Android 8.1 (API 27) → iOS Equivalent: iOS 12+
Architecture: Clean Architecture with MVVM + MVI Pattern

  ---
🎯 MAIN KEY FEATURES

1. Timer Functionality

- Three Timer Types:
  - Pomodoro (Focus Time): Default 25 minutes
  - Short Break: Default 5 minutes
  - Long Break: Default 15 minutes
- Timer Controls: Start, Pause, Resume, Stop, Skip
- Circular Progress Indicator: Animated timer display
- Background Timer: Continues running when app is backgrounded
- Auto-transition: Automatically cycles between work and breaks
- Cycle System: Long break after 4 completed pomodoros

2. Four Timer Style Themes

- Regular/Study: Clean, minimalist design
- Meditation: Calming purple/blue gradient with breathing animation
- Coding: VS Code/terminal-inspired with monospace fonts
- Reading: Book-themed with serif fonts and warm sepia tones

3. Statistics & Analytics

- Daily statistics (completed pomodoros, focus time, cycles)
- Weekly statistics with bar charts and line charts
- Date selector for historical data
- Session history listing
- Weekly average calculations

4. Customization Settings

- Adjustable timer durations (5-60 min for pomodoro, 1-30 min for short break, 5-60 min for long break)
- Pomodoros before long break (1-10, default 4)
- Vibration toggle
- Sound toggle
- Focus mode (distraction-free)
- Theme selector (System/Light/Dark)

5. Notifications

- Completion notifications with sound/vibration
- Foreground persistent notification showing remaining time
- Updates every 5 seconds
- Motivational quotes on completion

6. Focus Mode

- Shows "🎯 Focus Mode Active" indicator
- Minimizes distractions during sessions

  ---
🎨 COLOR PALETTE

Primary App Colors

Light Theme

// Main Colors
primary = Color(0xFFFF5757)         // Vibrant Coral Red (Pomodoro)
onPrimary = Color.white
primaryContainer = Color(0xFFFFECEA) // Light Coral background
onPrimaryContainer = Color(0xFF690013)

secondary = Color(0xFF4ECDC4)       // Teal (Short Break)
onSecondary = Color.white
secondaryContainer = Color(0xFFCDF7F3)
onSecondaryContainer = Color(0xFF003936)

tertiary = Color(0xFF7B69EE)        // Purple (Long Break)
onTertiary = Color.white
tertiaryContainer = Color(0xFFEAE5FF)
onTertiaryContainer = Color(0xFF2F2274)

// Backgrounds & Surfaces
background = Color(0xFFFAFAFA)
onBackground = Color(0xFF1C1B1F)
surface = Color(0xFFFFFFFF)
onSurface = Color(0xFF1C1B1F)
surfaceVariant = Color(0xFFF3F3F3)
onSurfaceVariant = Color(0xFF49454F)
outline = Color(0xFFD8D8D8)

// Navigation Icons
statsIconColor = Color(0xFFFF5722)     // Deep Orange
timerStyleIconColor = Color(0xFF27C93F) // Green
settingsIconColor = Color(0xFF2196F3)  // Blue

Dark Theme

// Main Colors
primary = Color(0xFFFF6B6B)         // Brighter Coral (Pomodoro)
onPrimary = Color.white
primaryContainer = Color(0xFF690013)
onPrimaryContainer = Color(0xFFFFECEA)

secondary = Color(0xFF72EFEA)       // Bright Teal (Short Break)
onSecondary = Color.black
secondaryContainer = Color(0xFF003936)
onSecondaryContainer = Color(0xFFCDF7F3)

tertiary = Color(0xFF988FFF)        // Bright Purple (Long Break)
onTertiary = Color.black
tertiaryContainer = Color(0xFF2F2274)
onTertiaryContainer = Color(0xFFEAE5FF)

// Backgrounds & Surfaces
background = Color(0xFF121212)
onBackground = Color(0xFFE5E1E6)
surface = Color(0xFF1E1E1E)
onSurface = Color(0xFFE5E1E6)
surfaceVariant = Color(0xFF2D2D2D)
onSurfaceVariant = Color(0xFFCAC4D0)
outline = Color(0xFF444444)

Timer Style Theme Colors

1. Study/Regular Theme

// Dark Mode
studyDarkBackground = Color(0xFF0A0A0A)
studyDarkPrimary = Color(0xFFFF6B6B)
studyDarkText = Color.white
studyDarkTextSecondary = Color(0xFF666666)
studyDarkBorder = Color(0xFF333333)

// Light Mode
studyLightBackground = Color(0xFFF8F9FA)
studyLightPrimary = Color(0xFFE74C3C)
studyLightText = Color(0xFF2C3E50)
studyLightTextSecondary = Color(0xFF6C757D)
studyLightBorder = Color(0xFFDEE2E6)

2. Reading Theme (Sepia/Book Style)

// Dark Mode - Sepia/Warm Tones
readingDarkBackgroundStart = Color(0xFF2D1B00)
readingDarkBackgroundEnd = Color(0xFF1A0F00)
readingDarkBookStart = Color(0xFFD4A574)      // Book gradient
readingDarkBookEnd = Color(0xFFC89B5E)
readingDarkText = Color(0xFFF4E4C1)
readingDarkTitle = Color(0xFFD4A574)
readingDarkSubtitle = Color(0xFF8B7355)
readingDarkTimerText = Color(0xFF2D1B00)

// Light Mode - Cream/Beige Tones
readingLightBackgroundStart = Color(0xFFFFF9E6)
readingLightBackgroundEnd = Color(0xFFFEF5D9)
readingLightBookStart = Color(0xFFFFE4B5)
readingLightBookEnd = Color(0xFFFFD89B)
readingLightText = Color(0xFF5D4E37)
readingLightTitle = Color(0xFF8B6F47)
readingLightSubtitle = Color(0xFFA0826D)

3. Meditation Theme

// Dark Mode - Purple Gradient
meditationDarkBackgroundStart = Color(0xFF2A2A40)
meditationDarkBackgroundEnd = Color(0xFF1A1A2E)
meditationDarkPrimary = Color(0xFF8A76FF)
meditationDarkBreatheText = Color(0xFFA5A5D0)
meditationDarkText = Color.white
meditationDarkDotInactive = Color(0xFF3A3A50)
meditationDarkButtonBorder = Color(0xFF4A4A60)

// Light Mode - Blue Gradient
meditationLightBackgroundStart = Color(0xFFF0F4FF)
meditationLightBackgroundEnd = Color(0xFFE6EFFF)
meditationLightPrimary = Color(0xFF6366F1)
meditationLightBreatheText = Color(0xFF6B7FA8)
meditationLightText = Color(0xFF2C3E50)
meditationLightDotInactive = Color(0xFFD1D5F0)
meditationLightButtonBorder = Color(0xFFC7CBDE)

4. Coding Theme (VS Code Style)

// Dark Mode - VS Code Dark
codingDarkBackground = Color(0xFF1E1E1E)
codingDarkTerminalBg = Color(0xFF252526)
codingDarkPrimary = Color(0xFF4EC9B0)
codingDarkText = Color(0xFFD4D4D4)
codingDarkPrompt = Color(0xFF4EC9B0)
codingDarkCommand = Color(0xFF569CD6)
codingDarkComment = Color(0xFF6A9955)
codingDarkString = Color(0xFFCE9178)
codingDarkKeyword = Color(0xFFC586C0)
codingDarkDotRed = Color(0xFFFF5F56)
codingDarkDotYellow = Color(0xFFFFBD2E)
codingDarkDotGreen = Color(0xFF27C93F)

// Light Mode - Atom Light
codingLightBackground = Color(0xFFF5F5F5)
codingLightTerminalBg = Color.white
codingLightPrimary = Color(0xFF0184BC)
codingLightText = Color(0xFF383A42)
codingLightCommand = Color(0xFF4078F2)
codingLightComment = Color(0xFFA0A1A7)
codingLightString = Color(0xFF50A14F)
codingLightKeyword = Color(0xFFA626A4)

  ---
🔤 TYPOGRAPHY & FONTS

Font Families Required

1. Roboto (Primary Font)

- Weights needed:
  - Light (300)
  - Regular (400)
  - Medium (500)
  - Bold (700)
- Usage: Default UI, timer display (Regular theme), buttons, body text

2. Libre Baskerville (Serif Font)

- Weights needed:
  - Regular (400)
  - Italic (400)
  - Bold (700)
- Usage: Reading theme titles, book-style text

3. JetBrains Mono (Monospace Font)

- Weights needed:
  - Regular (400)
  - Medium (500)
  - SemiBold (600)
  - Bold (700)
- Usage: Coding theme, terminal-style text

Typography Styles

Default Typography (Material 3)

// Titles
titleLarge: Roboto SemiBold, 18sp, 0sp letter spacing
titleMedium: Roboto Medium, 16sp, 0.15sp letter spacing
titleSmall: Roboto Medium, 14sp, 0.1sp letter spacing

// Body
bodyLarge: Roboto Regular, 16sp, 0.5sp letter spacing
bodyMedium: Roboto Regular, 14sp, 0.25sp letter spacing
bodySmall: Roboto Regular, 12sp, 0.4sp letter spacing

// Labels
labelLarge: Roboto Medium, 14sp, 0.1sp letter spacing
labelMedium: Roboto Medium, 12sp, 0.5sp letter spacing
labelSmall: Roboto Medium, 11sp, 0.5sp letter spacing

// Headlines
headlineLarge: Roboto Bold, 32sp, 0sp letter spacing
headlineMedium: Roboto Bold, 28sp, 0sp letter spacing
headlineSmall: Roboto Bold, 24sp, 0sp letter spacing

Study/Regular Timer Typography

timer: Roboto Bold, 100sp, 1sp letter spacing
statValue: Roboto Bold, 18sp
statLabel: Roboto Medium, 14sp, 1.5sp letter spacing (UPPERCASE)
buttonIcon: 28sp
buttonLabel: Roboto Medium, 9sp, 0.5sp letter spacing

Reading Timer Typography

title: Libre Baskerville Light, 32sp (e.g., "Reading Session")
subtitle: Libre Baskerville Italic, 15sp
timer: Roboto Bold, 80sp
pageStatus: Libre Baskerville Regular, 14sp
statValue: Libre Baskerville SemiBold, 24sp
statLabel: Libre Baskerville Regular, 12sp
button: Libre Baskerville Medium, 14sp
statusText: Libre Baskerville Italic, 14sp

Meditation Timer Typography

timer: Roboto Light, 100sp (KEY: Light weight for calm effect!)
breatheText: Roboto Regular, 18sp
buttonIcon: 24sp

Coding Timer Typography

timer: JetBrains Mono Bold, 100sp
terminal: JetBrains Mono Regular, 14sp
comment: JetBrains Mono Regular, 12sp
statKey: JetBrains Mono Regular, 14sp
statValue: JetBrains Mono SemiBold, 18sp
button: JetBrains Mono Regular, 13sp

  ---
📐 SCREEN LAYOUTS & NAVIGATION

Navigation Structure

MainView (TabView or NavigationView)
├─ TimerScreen (Start Screen)
│   ├─ Circular Timer Component
│   ├─ Timer Type Tabs (Pomodoro, Short Break, Long Break)
│   ├─ Control Buttons (Start/Pause/Resume/Stop/Skip)
│   ├─ Session Summary Card
│   └─ Top Bar with navigation to:
│       ├─ Settings
│       ├─ Statistics
│       └─ Timer Styles
│
├─ StatisticsScreen
│   ├─ Date Selector
│   ├─ Daily Stats Section
│   ├─ Weekly Charts (Bar & Line charts)
│   ├─ Weekly Average Stats
│   └─ Session History List
│
├─ SettingsScreen
│   ├─ Timer Durations Section
│   │   ├─ Pomodoro Duration (5-60 min, default 25)
│   │   ├─ Short Break (1-30 min, default 5)
│   │   ├─ Long Break (5-60 min, default 15)
│   │   └─ Pomodoros Before Long Break (1-10, default 4)
│   ├─ Notifications Section
│   │   ├─ Sound Toggle
│   │   └─ Vibration Toggle
│   ├─ Focus Mode Toggle
│   ├─ Theme Selector (System/Light/Dark)
│   └─ Reset to Defaults Button
│
└─ TimerStylesScreen
└─ List of 4 timer styles with previews
├─ Regular
├─ Meditation
├─ Coding
└─ Reading

UI Component Sizes

// Timer Circle
timerCircleSizePhone = 0.8 * screenWidth
timerCircleSizeTablet = 0.7 * screenWidth

// Tablet Layouts
tabletContentWidth = 0.9 * screenWidth
tabletControlsWidth = 0.8 * screenWidth

// Timer Text Size
timerTextSizeDefault = 100sp (adjust for iOS)

// Button Sizes
controlButtonSize = ~60x60 points
iconSize = 24-28 points

// Spacing
defaultPadding = 16 points
largePadding = 24 points
smallPadding = 8 points

  ---
💾 DATA MODELS

Core Models

// Timer Types Enum
enum TimerType: String {
case pomodoro = "POMODORO"
case shortBreak = "SHORT_BREAK"
case longBreak = "LONG_BREAK"
}

// Timer States Enum
enum TimerState {
case idle
case running
case paused
case completed
}

// Timer Style Enum
enum TimerStyle: String {
case regular = "REGULAR"
case meditation = "MEDITATION"
case coding = "CODING"
case reading = "READING"
}

// Theme Mode Enum
enum ThemeMode: String {
case system = "SYSTEM"
case light = "LIGHT"
case dark = "DARK"
}

// Pomodoro Session Model
struct PomodoroSession: Codable, Identifiable {
var id: Int64
var timerType: TimerType
var plannedDurationSeconds: Int64
var focusedDurationSeconds: Int64
var isCompleted: Bool
var startedAt: Date
}

// Settings Model
struct PomodoroSettings: Codable {
var pomodoroMinutes: Int = 25
var shortBreakMinutes: Int = 5
var longBreakMinutes: Int = 15
var pomodoroCycleLength: Int = 4
var vibrationEnabled: Bool = true
var soundEnabled: Bool = true
var enableFocusMode: Bool = false
}

// Daily Stats Model
struct DailyStats {
var date: Date
var completedPomodoros: Int
var totalFocusMinutes: Int
var completedCycles: Int
}

// Weekly Stats Model
struct WeeklySessionStats {
var totalSessions: Int
var activeDays: Int
var averageSessionsPerDay: Double
}

  ---
⚙️ KEY CONSTANTS & SETTINGS

// Default Durations (minutes)
let DEFAULT_POMODORO_MINUTES = 25
let DEFAULT_SHORT_BREAK_MINUTES = 5
let DEFAULT_LONG_BREAK_MINUTES = 15
let DEFAULT_POMODORO_CYCLE_LENGTH = 4

// Timer Settings Ranges
let POMODORO_MIN = 5, POMODORO_MAX = 60
let SHORT_BREAK_MIN = 1, SHORT_BREAK_MAX = 30
let LONG_BREAK_MIN = 5, LONG_BREAK_MAX = 60
let CYCLE_LENGTH_MIN = 1, CYCLE_LENGTH_MAX = 10

// Timer Intervals
let TIMER_TICK_INTERVAL = 1.0 // seconds
let NOTIFICATION_UPDATE_INTERVAL = 5.0 // seconds
let AUTO_TRANSITION_DELAY = 2.0 // seconds

// UI Scaling
let TIMER_CIRCLE_SIZE_PHONE = 0.8
let TIMER_CIRCLE_SIZE_TABLET = 0.7
let TABLET_CONTENT_WIDTH = 0.9
let TABLET_CONTROLS_WIDTH = 0.8

  ---
🔔 NOTIFICATIONS

Notification Types

1. Persistent Timer Notification
   - Shows remaining time
   - Updates every 5 seconds
   - Displays timer type (Pomodoro/Break)
   - Shows pause status
2. Completion Notifications
   - Title: "Focus session completed!" / "Break time over!" / "Long break completed!"
   - Body: Completion message
   - Sound (if enabled)
   - Haptic feedback (if enabled)

Motivational Quotes (shown after completion)

"The secret to getting ahead is getting started."
"Focus on being productive instead of busy."
"The way to get started is to quit talking and begin doing."
"It's not about having time, it's about making time."
"You don't have to be great to start, but you have to start to be great."
"Productivity is never an accident. It is always the result of commitment to excellence."

  ---
📊 STATISTICS FEATURES

Metrics to Track

- Total completed pomodoros
- Total focus time (minutes/hours)
- Completed cycles (4 pomodoros = 1 cycle)
- Daily session count
- Weekly session count
- Weekly average sessions per day
- Active days in week

Chart Types Needed

1. Bar Chart: Daily sessions for the week
2. Line Chart: Weekly trend of focus time
3. Stats Cards: Display key metrics with icons

  ---
🎵 SOUNDS & HAPTICS

Sound Events

- Timer completion (requires a notification sound)
- Can be toggled on/off in settings

Haptic Feedback Events

- Timer completion (vibration pattern)
- Button taps (light haptic)
- Can be toggled on/off in settings

  ---
💡 ADDITIONAL IMPLEMENTATION NOTES

Background Timer

- Must continue running when app is backgrounded
- Use iOS Background Modes: "audio" or timer API
- Show persistent notification during active timer

Data Persistence

- Use CoreData or SwiftData for session history
- Use UserDefaults or AppStorage for settings
- Store timer state to resume after app restart

Animations

- Circular timer progress: Smooth animation
- Timer type tabs: Slide transition
- Charts: Animated drawing
- Theme transitions: Fade animation

Responsive Design

- Support iPhone (all sizes)
- Support iPad with larger timer circle
- Use size classes for adaptive layouts
- Safe area insets for notched devices

Accessibility

- VoiceOver support for all controls
- Dynamic Type support for text scaling
- High contrast mode support
- Reduce motion support for animations

  ---
📁 ASSETS NEEDED

Icons (SF Symbols or custom)

- Play/Start button
- Pause button
- Stop button
- Skip button
- Settings icon (gear)
- Statistics icon (chart)
- Timer styles icon
- Back/Close button

Sounds

- Timer completion sound (bell/chime)

App Icon

- Design based on Pomodoro timer concept
- Multiple sizes for iOS (1024x1024 for App Store)

  ---
This comprehensive guide contains everything you need to build a pixel-perfect iOS version of the Pomodoro Focus Timer app. The architecture is clean and well-documented,
making it straightforward to translate to SwiftUI with MVVM pattern.
