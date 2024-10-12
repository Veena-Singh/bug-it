package com.example.bugit.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bugit.navigation.BottomBar
import com.example.bugit.viewmodel.MainViewModel

@Composable
fun HomeScreen(paddingModifier: Modifier, navController: NavHostController, viewModel: MainViewModel) {
    val uiState = viewModel.mainUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value.imageUri) {
        if (uiState.value.imageUri != "null") {
            navController.navigate("${BottomBar.BugSubmission.route}/${uiState.value.imageUri}")
        }
    }

    Box(
        modifier = paddingModifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(text="home", fontSize = 100.sp)
    }
}