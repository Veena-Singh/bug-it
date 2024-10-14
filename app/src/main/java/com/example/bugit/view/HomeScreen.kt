package com.example.bugit.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bugit.R
import com.example.bugit.navigation.BottomBar
import com.example.bugit.navigation.defaultNavOptions
import com.example.bugit.util.Constant
import com.example.bugit.util.Constant.NULL
import com.example.bugit.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    paddingModifier: Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val uiState = viewModel.mainUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value.imageUri) {
        if (uiState.value.imageUri != NULL) {
            navController.navigate(BottomBar.BugSubmission.createRoute(imageUri = uiState.value.imageUri)) {
                defaultNavOptions(navController)
            }
            viewModel.setImageUri(NULL)
        }
    }

    Box(
        modifier = paddingModifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(Constant.PADDING_20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = Constant.WELCOME,
                style = TextStyle(fontSize = Constant.FONT_40, fontWeight = FontWeight.Bold)
            )
            Text(
                text = Constant.IN,
                style = TextStyle(fontSize = Constant.FONT_30, fontWeight = FontWeight.Bold)
            )
            Text(
                text = Constant.BUG_IT,
                style = TextStyle(fontSize = Constant.FONT_20, fontWeight = FontWeight.Bold)
            )
            Text(
                text = Constant.APP_DESCRIPTION,
                style = TextStyle(fontSize = Constant.FONT_15),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Constant.PADDING_20)
            )
            Image(
                painter = painterResource(id = R.drawable.bugit),
                contentDescription = null,
                modifier = Modifier
                    .size(Constant.PADDING_300),
            )
            Text(
                text = Constant.QUESTION_TEXT,
                style = TextStyle(fontSize = Constant.FONT_15, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = {
                    navController.navigate(BottomBar.BugSubmission.createRoute(imageUri = NULL)){
                        defaultNavOptions(navController)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.padding(top = Constant.PADDING_10)
            )
            {
                Text(text = Constant.START)
            }
        }
    }
}