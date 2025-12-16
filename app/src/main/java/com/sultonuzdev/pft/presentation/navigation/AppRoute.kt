package com.sultonuzdev.pft.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Route definitions for app navigation
 */

sealed class AppRoute {

    @Serializable
    data object Timer : AppRoute()


    @Serializable
    data object Statistics : AppRoute()

    @Serializable
    data object Settings : AppRoute()

    @Serializable
    data object TimerList : AppRoute()


}