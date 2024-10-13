package com.example.bugit.view

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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bugit.navigation.AppBottomBar
import com.example.bugit.navigation.BottomNavigationGraph
import com.example.bugit.util.Constant
import com.example.bugit.util.Constant.NULL
import com.example.bugit.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(imageUri: String) {
    val navController = rememberNavController()
    val appBarTitle = remember {
        mutableStateOf(Constant.SUBMIT_BUG_LABEL)
    }
    val mainViewModel: MainViewModel = viewModel()
    val currentScreen = navController.currentBackStackEntryAsState()

    LaunchedEffect(currentScreen.value?.destination?.route) {
        appBarTitle.value = when (currentScreen.value?.destination?.route) {
            Constant.SUBMIT_BUG_ROUTE_PARAMS -> Constant.SUBMIT_BUG_LABEL
            Constant.BUG_LIST_ROUTE -> Constant.BUG_LIST_LABEL
            else -> Constant.HOME_LABEL
        }
    }

    LaunchedEffect(Unit) {
        if (imageUri != NULL) {
            mainViewModel.setImageUri(imageUri)
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen.value?.destination?.route != Constant.HOME_ROUTE) {
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
            }
        },
        bottomBar = {
            AppBottomBar(
                mainViewModel, navController = navController
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