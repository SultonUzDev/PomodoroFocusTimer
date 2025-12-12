package com.sultonuzdev.pft.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Route definitions for app navigation
 */

sealed class AppRoute {

    @Serializable
    data object Home : AppRoute()



    @Serializable
    data object Statistics : AppRoute()

    @Serializable
    data object Settings : AppRoute()


    sealed class Timer: AppRoute() {
        @Serializable
        data object Regular : Timer()

        @Serializable
        data object Study : Timer()

        @Serializable
        data object Coding : Timer()

        @Serializable
        data object Reading : Timer()

        @Serializable
        data object Meditation : Timer()


    }

}