package com.example.bugit.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bugit.common.navigation.BottomBar
import com.example.bugit.view.uistate.BugSubmissionScreenUiState
import com.example.bugit.viewmodel.BugSubmissionViewModel

@Composable
fun Alert(
    title: String,
    message: String,
    bugSubmissionViewModel: BugSubmissionViewModel,
    uiState: State<BugSubmissionScreenUiState>,
    navController: NavHostController
) {

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