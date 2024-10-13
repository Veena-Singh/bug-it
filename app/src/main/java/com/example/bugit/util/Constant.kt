package com.example.bugit.util

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Constant {

    //BottomBar
    const val HOME_ROUTE = "home"
    const val SUBMIT_BUG_ROUTE = "submitBug"
    const val BUG_LIST_ROUTE = "bugsList"
    const val HOME_LABEL = "Home"
    const val SUBMIT_BUG_LABEL = "Submit Bug"
    const val BUG_LIST_LABEL = "Bugs List"

    // BugSubmissionScreen
    const val IMAGE = "IMAGE"
    const val DESCRIPTION = "DESCRIPTION"
    const val SUBMIT = "SUBMIT"
    const val IMAGE_PREFIX = "image/*"
    const val START = "CREATE BUG"
    const val ERROR_TITLE = "Something went wrong!"
    const val ERROR_MESSAGE = "Please retry, Bug has not submitted."
    const val SUCCESS_TITLE = "Great!"
    const val SUCCESS_MESSAGE = "You have successfully submitted bug."

    // Home Screen
    const val WELCOME = "Welcome"
    const val IN = "in"
    const val BUG_IT = "Bug It"
    const val APP_DESCRIPTION = "Bug it will help you in capturing your app bug and submit the bug."
    const val QUESTION_TEXT = "Do you want to submit a bug?"

    //Bug list screen
    const val LIST_TITLE_DESCRIPTION = "DESCRIPTION:"
    const val LIST_TITLE_DATE = "SUBMISSION DATE:"


    // Common
    const val NULL = "null"
    const val NULL_WITH_SPACE = "null "
    const val SUBMIT_BUG_ROUTE_PARAMS = "submitBug/{imageUri}"

    //View Paddings
    val PADDING_1 = 1.dp
    val PADDING_4 = 4.dp
    val PADDING_8 = 8.dp
    val PADDING_10 = 10.dp
    val PADDING_12 = 12.dp
    val PADDING_16 = 16.dp
    val PADDING_20 = 20.dp
    val PADDING_130 = 130.dp
    val PADDING_200 = 200.dp
    val PADDING_300 = 300.dp

    // Font size
    val FONT_40 = 40.sp
    val FONT_30 = 30.sp
    val FONT_20 = 20.sp
    val FONT_15 = 15.sp
    val FONT_10 = 10.sp

}