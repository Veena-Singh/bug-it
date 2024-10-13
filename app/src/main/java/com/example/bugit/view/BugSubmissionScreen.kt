package com.example.bugit.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.bugit.R
import com.example.bugit.navigation.BottomBar
import com.example.bugit.uistate.BugSubmissionScreenUiState
import com.example.bugit.util.Constant
import com.example.bugit.viewmodel.BugSubmissionViewModel

@Composable
fun BugSubmissionScreen(
    paddingModifier: Modifier,
    navController: NavHostController,
    bugImage: String
) {

    val bugSubmissionViewModel: BugSubmissionViewModel = viewModel()
    val uiState = bugSubmissionViewModel.uiState.collectAsStateWithLifecycle()
    val bugDescription = remember { mutableStateOf("") }
    val imageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val tag = "BugSubmissionScreen"

    LaunchedEffect(bugImage) {
        if (bugImage != Constant.NULL) {
            val uri = Uri.parse(Uri.decode(bugImage))
            imageUri.value = uri
        }
    }

    // Launch gallery to select the image
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { bugImageUri ->
                imageUri.value = bugImageUri
            }
        }
    )

    // Permission required to get account name for google sheet api authentication
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bugSubmissionViewModel.submitBug(imageUri.value, bugDescription.value, context)
        } else {
            Log.d(tag, "Read Permission not granted")
        }
    }

    Box(
        modifier = paddingModifier
            .padding(Constant.PADDING_20)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(Constant.PADDING_10)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                showBugImage(imageUri, galleryLauncher, uiState, bugSubmissionViewModel) // Image
                showBugDescription(bugDescription, uiState, bugSubmissionViewModel) // Description
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.value.loading) {
                    CircularProgressIndicator()
                }
                Button(
                    onClick = {
                        requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
                    },
                    modifier = Modifier.align(Alignment.BottomCenter),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                )
                {
                    Text(text = Constant.SUBMIT)
                }
            }
        }
        if (uiState.value.showSuccessDialog || uiState.value.showFailureDialog) {
            val title =
                if (uiState.value.showFailureDialog) Constant.ERROR_TITLE else Constant.SUCCESS_TITLE
            val message =
                if (uiState.value.showFailureDialog) Constant.ERROR_MESSAGE else Constant.SUCCESS_MESSAGE
            AlertDialog(
                onDismissRequest = {
                    bugSubmissionViewModel.handleDialog()
                },
                title = {
                    Text(
                        text = title,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )
                },
                text = { Text(text = message, style = TextStyle(fontSize = 15.sp)) },
                confirmButton = {
                    Button(
                        onClick = {
                            bugSubmissionViewModel.handleDialog()
                            if (uiState.value.showSuccessDialog) {
                                navController.navigate(BottomBar.BugsList.route)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text("OK")
                    }
                })
        }
    }
}

@Composable
fun showBugImage(
    imageUri: MutableState<Uri?>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    uiState: State<BugSubmissionScreenUiState>,
    bugSubmissionViewModel: BugSubmissionViewModel
) {
    Text(text = Constant.IMAGE, fontSize = Constant.FONT_20, color = Color.Gray)
    Card(
        modifier = Modifier
            .padding(Constant.PADDING_10)
            .size(width = Constant.PADDING_200, height = Constant.PADDING_200),
        elevation = CardDefaults.cardElevation(defaultElevation = Constant.PADDING_10),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(
            Constant.PADDING_1,
            if (uiState.value.showImageFieldError) Color.Red else Color.Transparent
        )
    ) {
        Box {
            if (imageUri.value != null) {
                if (uiState.value.showImageFieldError) {
                    bugSubmissionViewModel.showImageFieldError(imageUri.value)
                }
                AsyncImage(
                    model = imageUri.value,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(Constant.PADDING_4)
                        .fillMaxHeight()
                        .width(Constant.PADDING_200)
                        .clip(RoundedCornerShape(Constant.PADDING_12)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder),
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(Constant.PADDING_4)
                        .fillMaxHeight()
                        .width(Constant.PADDING_200)
                        .clip(RoundedCornerShape(Constant.PADDING_12)),
                )
            }
            Icon(imageVector = Icons.Rounded.Edit,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable {
                        galleryLauncher.launch(Constant.IMAGE_PREFIX)
                    })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showBugDescription(
    bugDescription: MutableState<String>,
    uiState: State<BugSubmissionScreenUiState>,
    bugSubmissionViewModel: BugSubmissionViewModel
) {
    Text(
        text = Constant.DESCRIPTION,
        fontSize = Constant.FONT_20,
        color = Color.Gray,
        modifier = Modifier.padding(top = Constant.PADDING_20)
    )
    Card(
        modifier = Modifier
            .padding(Constant.PADDING_10)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Constant.PADDING_10),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(
            Constant.PADDING_1,
            if (uiState.value.showDescriptionFieldError) Color.Red else Color.Transparent
        )
    ) {
        TextField(
            value = bugDescription.value,
            onValueChange = { description ->
                bugDescription.value = description
                if (uiState.value.showDescriptionFieldError) {
                    bugSubmissionViewModel.showBugDescriptionFieldError(bugDescription = bugDescription.value)
                }
            },
            modifier = Modifier
                .padding(Constant.PADDING_16),
        )
    }
}
