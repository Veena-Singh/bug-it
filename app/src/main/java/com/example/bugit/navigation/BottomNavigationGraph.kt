package com.example.bugit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bugit.view.BugSubmissionScreen
import com.example.bugit.view.BugsListScreen
import com.example.bugit.view.HomeScreen
import com.example.bugit.viewmodel.MainViewModel

@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
    paddingModifier: Modifier,
    mainViewModel: MainViewModel
) {

    NavHost(navController = navController,
        startDestination = BottomBar.Home.route
    ) {
        composable(route= BottomBar.Home.route) {
            HomeScreen(paddingModifier, navController, mainViewModel)
        }
        composable(route= BottomBar.BugSubmission.route+"/{imageUri}",
                arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) {
                backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            BugSubmissionScreen(paddingModifier, navController, imageUri.toString())
        }
        composable(route= BottomBar.BugsList.route) {
            BugsListScreen(paddingModifier, navController)
        }
    }
}