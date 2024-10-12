package com.example.bugit.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bugit.util.Constant.HOME_ROUTE
import com.example.bugit.util.Constant.SUBMIT_BUG_ROUTE
import com.example.bugit.viewmodel.MainViewModel

@Composable
fun AppBottomBar(
    mainViewModel: MainViewModel,
    navController: NavHostController,
//    onNavigateToDestination: (route: String) -> Unit
) {
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
                    if (route == HOME_ROUTE) {
                        mainViewModel.setImageUri("null")
                    }
                    if (route == SUBMIT_BUG_ROUTE) {
                        val param = "null"
                        route = "${BottomBar.BugSubmission.route}/${param} "
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
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

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

