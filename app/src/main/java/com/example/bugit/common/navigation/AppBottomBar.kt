package com.example.bugit.common.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bugit.common.util.Constant
import com.example.bugit.common.util.Constant.HOME_ROUTE
import com.example.bugit.common.util.Constant.NULL
import com.example.bugit.viewmodel.MainViewModel

@Composable
fun AppBottomBar(
    mainViewModel: MainViewModel,
    navController: NavHostController,
) {
    val screens = listOf(
        BottomBar.Home,
        BottomBar.BugSubmission,
        BottomBar.BugsList
    )
    BottomNavigation() {
        val currentRoute = currentRoute(navController)
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Image(
                        painter = painterResource(id = screen.icon), contentDescription = null,
                        modifier = Modifier.size(Constant.PADDING_20),
                        colorFilter = if (currentRoute == screen.route) ColorFilter.tint(Color.White) else ColorFilter.tint(
                            Color.LightGray
                        )
                    )
                },
                label = { Text(screen.label) },
                onClick = {
                    when (screen.route) {
                         BottomBar.BugSubmission.route -> {
                            navController.navigate(BottomBar.BugSubmission.createRoute(imageUri = NULL))
                            {
                                defaultNavOptions(navController)
                            }
                        }
                        else  -> {
                            if (screen.route == HOME_ROUTE) {
                                mainViewModel.setImageUri(NULL)
                            }
                            navController.navigate(screen.route)
                            {
                                defaultNavOptions(navController)
                            }
                        }
                    }
                },
                selected = currentRoute == screen.route
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

