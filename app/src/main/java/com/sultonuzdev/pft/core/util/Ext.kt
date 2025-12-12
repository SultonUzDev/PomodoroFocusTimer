package com.sultonuzdev.pft.core.util

private fun shouldShowBottomNavigation(currentRoute: String?): Boolean {
    if (currentRoute == null) return false

    // Only show bottom nav on the start destinations of each navigation graph
    val startDestinations = setOf(
        "Timer.Regular",        // Start destination of HomeJobsGraphRoute
        "GigJobRoutes.List",     // Start destination of GigJobsGraphRoute
        "JobPostingRoutes.TypeSelection",  // Start destination of PostJobsGraphRoute
        "ChatRoutes.List",    // Start destination of ChatGraphRoute
        "ProfileRoutes.Main"      // Start destination of ProfileGraphRoute
    )

    // Check if current route is one of the start destinations
    return startDestinations.any { routeName ->
        currentRoute.contains(routeName, ignoreCase = true)
    }
}