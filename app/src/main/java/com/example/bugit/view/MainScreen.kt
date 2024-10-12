package com.example.bugit.view

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bugit.navigation.AppBottomBar
import com.example.bugit.navigation.BottomNavigationGraph
import com.example.bugit.util.Constant
import com.example.bugit.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(imageUri: String) {
    val navController = rememberNavController()
    val appBarTitle = remember {
        mutableStateOf(Constant.HOME_LABEL)
    }
    val mainViewModel: MainViewModel = viewModel()

    LaunchedEffect(Unit) {
        if (imageUri != "null") {
            mainViewModel.setImageUri(imageUri.toString())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = appBarTitle.value,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = Constant.FONT_20,
                    )
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Blue
                )
            )
        },
        bottomBar = {
            AppBottomBar(mainViewModel, navController = navController,
//                onNavigateToDestination = {
//                    appBarTitle.value = when (it) {
//                        Constant.HOME_ROUTE -> Constant.HOME_LABEL
//                        Constant.SUBMIT_BUG_ROUTE -> Constant.SUBMIT_BUG_LABEL
//                        else -> {
//                            Constant.BUG_LIST_LABEL
//                        }
//                    }
//                    navController.navigate(it) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        restoreState = true
//                        launchSingleTop = true
//                    }
//                }
            )
        },
    )
    { paddingValues ->
        BottomNavigationGraph(
            navController = navController,
            paddingModifier = Modifier.padding(paddingValues),
            mainViewModel = mainViewModel
        )
    }
}