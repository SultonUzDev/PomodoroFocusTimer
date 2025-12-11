package com.sultonuzdev.pft.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sultonuzdev.pft.presentation.settings.SettingsScreenRoot
import com.sultonuzdev.pft.presentation.stats.StatsScreenRoot
import com.sultonuzdev.pft.presentation.timer.TimerScreen
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
        startDestination = AppRoute.Timer,
        modifier = modifier
    ) {
        composable<AppRoute.Timer> {
            TimerScreen(
                navigateToSettings = { navController.navigate(AppRoute.Settings) },
                navigateToStats = { navController.navigate(AppRoute.Statistics) },
                navigateToStyles = { navController.navigate(AppRoute.TimerStyles) }
            )
        }

        composable<AppRoute.Statistics> {
            StatsScreenRoot(
                onBackClick = { navController.navigateUp() },
            )
        }

        composable<AppRoute.Settings> {
            SettingsScreenRoot(
                onBackClick = { navController.navigateUp() },

                )
        }

        composable<AppRoute.TimerStyles> {
            TimerListScreen(
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}