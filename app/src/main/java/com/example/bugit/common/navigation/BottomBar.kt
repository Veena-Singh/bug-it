package com.example.bugit.common.navigation

import com.example.bugit.R
import com.example.bugit.common.util.Constant
import com.example.bugit.common.util.Constant.SUBMIT_BUG_ROUTE

sealed class BottomBar(
    val route: String,
    val label: String,
    val icon: Int
) {

    object Home : BottomBar(
        route = Constant.HOME_ROUTE,
        label = Constant.HOME_LABEL,
        icon = R.drawable.home
    )

    object BugSubmission : BottomBar(
        route = Constant.SUBMIT_BUG_ROUTE_PARAMS,
        label = Constant.SUBMIT_BUG_LABEL,
        icon = R.drawable.bug
    ) {
        fun createRoute(imageUri: String) = "$SUBMIT_BUG_ROUTE/$imageUri"
    }

    object BugsList : BottomBar(
        route = Constant.BUG_LIST_ROUTE,
        label = Constant.BUG_LIST_LABEL,
        icon = R.drawable.buglist
    )
}