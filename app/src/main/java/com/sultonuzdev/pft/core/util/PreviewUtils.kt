package com.sultonuzdev.pft.core.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.presentation.timer_list.TimerCard
import com.sultonuzdev.pft.presentation.timer_list.timerOptions


@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
annotation class DarkPreview

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true,
    device = "spec:width=411dp,height=891dp"

)
annotation class LightPreview


@LightPreview
@DarkPreview
annotation class AppPreview


@AppPreview
@Composable
fun MyComposablePreview() {
    PomodoroTheme {
        TimerCard(option = timerOptions.first()) {}
    }
}