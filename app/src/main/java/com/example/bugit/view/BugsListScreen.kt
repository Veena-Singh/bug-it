package com.example.bugit.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun BugsListScreen(paddingModifier: Modifier) {
    Box(
        modifier = paddingModifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(text = "Bug list", fontSize = 100.sp)
    }
}