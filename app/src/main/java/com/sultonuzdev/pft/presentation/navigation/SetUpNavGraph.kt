package com.sultonuzdev.pft.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.presentation.settings.SettingsScreen
import com.sultonuzdev.pft.presentation.stats.StatsScreen
import com.sultonuzdev.pft.presentation.timer.screens.coding.CodingTimerScreen
import com.sultonuzdev.pft.presentation.timer.screens.meditation.MeditationTimerScreen
import com.sultonuzdev.pft.presentation.timer.screens.reading.ReadingTimerScreen
import com.sultonuzdev.pft.presentation.timer.screens.regular.RegularTimerScreen
import com.sultonuzdev.pft.presentation.timer.screens.study.StudyTimerScreen
import com.sultonuzdev.pft.presentation.timer_styles.TimerListScreen


/**
 * Main navigation graph composable
 */
@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home,
        modifier = modifier
    ) {
        composable<AppRoute.Home> {
            TimerListScreen(
                navigateToSettings = { navController.navigate(AppRoute.Settings) },
                navigateToStats = { navController.navigate(AppRoute.Statistics) },
                navigateToTimer = { type ->
                    when (type) {
                        TimerStyle.MEDITATION -> navController.navigate(AppRoute.Timer.Meditation)
                        TimerStyle.STUDY -> navController.navigate(AppRoute.Timer.Study)
                        TimerStyle.READING -> navController.navigate(AppRoute.Timer.Reading)
                        TimerStyle.CODING -> navController.navigate(AppRoute.Timer.Coding)
                        TimerStyle.REGULAR -> navController.navigate(AppRoute.Timer.Regular)

                    }

                }
            )
        }

        composable<AppRoute.Statistics> {
            StatsScreen(onBackClick = { navController.navigateUp() })
        }

        composable<AppRoute.Settings> {
            SettingsScreen(onBackClick = { navController.navigateUp() })
        }


        // timers

        composable<AppRoute.Timer.Regular> {
            RegularTimerScreen()
        }
        composable<AppRoute.Timer.Meditation> {
            MeditationTimerScreen()
        }
        composable<AppRoute.Timer.Study> {
            StudyTimerScreen()
        }
        composable<AppRoute.Timer.Reading> {
            ReadingTimerScreen()
        }
        composable<AppRoute.Timer.Coding> {
            CodingTimerScreen()
        }


    }
}