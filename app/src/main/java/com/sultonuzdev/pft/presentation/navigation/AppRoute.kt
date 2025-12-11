package com.sultonuzdev.pft.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Route definitions for app navigation
 */

sealed interface AppRoute {

    @Serializable
    data object Timer : AppRoute

    @Serializable
    data object Statistics : AppRoute

    @Serializable
    data object Settings : AppRoute

    @Serializable
    data object TimerStyles: AppRoute
}