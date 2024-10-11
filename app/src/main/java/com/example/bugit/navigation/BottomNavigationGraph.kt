package com.example.bugit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bugit.view.BugSubmissionScreen
import com.example.bugit.view.BugsListScreen
import com.example.bugit.view.HomeScreen

@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
    paddingModifier: Modifier
) {

    NavHost(navController = navController,
        startDestination = BottomBar.Home.route
    ) {
        composable(route= BottomBar.Home.route) {
            HomeScreen(paddingModifier)
        }
        composable(route= BottomBar.Tasks.route) {
            BugSubmissionScreen(paddingModifier)
        }
        composable(route= BottomBar.Options.route) {
            BugsListScreen(paddingModifier)
        }
    }
}