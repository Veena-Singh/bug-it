package com.example.bugit.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bugit.R
import com.example.bugit.viewmodel.BugListViewModel

@Composable
fun BugsListScreen(paddingModifier: Modifier) {

    val bugListViewModel: BugListViewModel = viewModel()
    val uiState = bugListViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        bugListViewModel.getAllBugList(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.value.loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = paddingModifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.value.bugList) { item ->
                    val sheetName = item.sheetName
                    item.data.forEach {
                        BugCard(
                            description = it[0].toString(),
                            date = sheetName,
                            image = it[1].toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BugCard(description: String, date: String, image: String) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit ,
                placeholder = painterResource(id = R.drawable.placeholder),
            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = "DESCRIPTION:",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = description,
                )
                Divider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                Text(
                    text = "Submission Date:",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = date,
                )
            }
        }
    }
}