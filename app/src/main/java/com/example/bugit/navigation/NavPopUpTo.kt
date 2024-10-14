package com.example.bugit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavOptionsBuilder.defaultNavOptions(navController: NavController) {
    popUpTo(navController.graph.startDestinationId) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}