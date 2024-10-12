package com.example.bugit.navigation

import android.util.Log
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bugit.util.Constant.SUBMIT_BUG_ROUTE

@Composable
fun AppBottomBar(navController: NavHostController,
                 onNavigateToDestination: (route: String) -> Unit) {
    val screens = listOf(
        BottomBar.Home,
        BottomBar.BugSubmission,
        BottomBar.BugsList
    )
    BottomNavigation() {
        val currentRoute = currentRoute(navController)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { screen.label },
                onClick = {
//                    onNavigateToDestination(screen.route)
                    var route = screen.route
                    if(route == SUBMIT_BUG_ROUTE) {
                        val param = null
                        route = "$SUBMIT_BUG_ROUTE/$param"
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selected = /*currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        screen.route,
                        null
                    )
                } == true*/                 currentRoute == screen.route,

                )
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

