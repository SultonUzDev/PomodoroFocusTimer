package com.sultonuzdev.pft.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sultonuzdev.pft.presentation.settings.SettingsScreenRoot
import com.sultonuzdev.pft.presentation.stats.StatsScreenRoot
import com.sultonuzdev.pft.presentation.timer.TimerScreenRoot


/**
 * Main navigation graph composable
 */


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
        startDestination = AppRoute.TIMER,
        modifier = modifier
    ) {
        composable<AppRoute.TIMER> {
            TimerScreenRoot(
                navigateToSettings = { navController.navigate(AppRoute.SETTINGS) },
                navigateToStats = { navController.navigate(AppRoute.STATS) }
            )
        }

        composable<AppRoute.STATS> {
            StatsScreenRoot(
                onBackClick = { navController.navigateUp() },
            )
        }

        composable<AppRoute.SETTINGS> {
            SettingsScreenRoot(
                onBackClick = { navController.navigateUp() },

                )
        }
    }
}