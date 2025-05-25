package com.sultonuzdev.pft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sultonuzdev.pft.features.settings.presentation.SettingsScreenRoot
import com.sultonuzdev.pft.features.stats.presentation.stats.StatsScreenRoot
import com.sultonuzdev.pft.features.timer.presentation.TimerScreenRoot


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