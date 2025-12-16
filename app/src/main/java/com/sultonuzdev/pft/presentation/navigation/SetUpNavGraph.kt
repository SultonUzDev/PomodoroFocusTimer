package com.sultonuzdev.pft.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sultonuzdev.pft.presentation.settings.SettingsScreen
import com.sultonuzdev.pft.presentation.stats.StatsScreen
import com.sultonuzdev.pft.presentation.timer.screens.TimerScreen
import com.sultonuzdev.pft.presentation.timer_list.TimerListScreen


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
        startDestination = AppRoute.Timer,
        modifier = modifier
    ) {
        composable<AppRoute.Timer> {
            TimerScreen(
                navigateToSettings = { navController.navigate(AppRoute.Settings) },
                navigateToStats = { navController.navigate(AppRoute.Statistics) },
                navigateToTimerStyle = {
                    navController.navigate(AppRoute.TimerList)

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

        composable<AppRoute.TimerList> {
            TimerListScreen(navigateBack = { navController.navigateUp() })
        }


    }
}