package com.sultonuzdev.pft.navigation

import kotlinx.serialization.Serializable

/**
 * Route definitions for app navigation
 */

sealed class AppRoute {

    @Serializable
    data object TIMER : AppRoute()

    @Serializable
    data object STATS : AppRoute()

    @Serializable
    data object SETTINGS : AppRoute()



}