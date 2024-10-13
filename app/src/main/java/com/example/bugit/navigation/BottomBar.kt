package com.example.bugit.navigation

import com.example.bugit.R
import com.example.bugit.util.Constant

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
        route = Constant.SUBMIT_BUG_ROUTE,
        label = Constant.SUBMIT_BUG_LABEL,
        icon = R.drawable.bug
    )

    object BugsList : BottomBar(
        route = Constant.BUG_LIST_ROUTE,
        label = Constant.BUG_LIST_LABEL,
        icon = R.drawable.buglist
    )
}