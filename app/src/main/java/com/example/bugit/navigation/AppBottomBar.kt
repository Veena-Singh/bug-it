package com.example.bugit.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(navController: NavHostController,
                 onNavigateToDestination: (route: String) -> Unit) {
    val screens = listOf(
        BottomBar.Home,
        BottomBar.Tasks,
        BottomBar.Options
    )
    BottomNavigation() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { screen.label },
                onClick = {
                    onNavigateToDestination(screen.route)
                },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        screen.route,
                        null
                    )
                } == true
            )
        }
    }
}