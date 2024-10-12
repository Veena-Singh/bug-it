package com.example.bugit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bugit.util.Constant

sealed class BottomBar(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomBar(
        route = Constant.HOME_ROUTE,
        label = Constant.HOME_LABEL,
        icon = Icons.Rounded.Home
    )

    object BugSubmission : BottomBar(
        route = Constant.SUBMIT_BUG_ROUTE,
        label = Constant.SUBMIT_BUG_LABEL,
        icon = Icons.Rounded.CheckCircle
    )

    object BugsList : BottomBar(
        route = Constant.BUG_LIST_ROUTE,
        label = Constant.BUG_LIST_LABEL,
        icon = Icons.Rounded.Settings
    )
}