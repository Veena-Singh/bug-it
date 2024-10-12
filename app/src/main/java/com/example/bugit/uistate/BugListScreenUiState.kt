package com.example.bugit.uistate

import com.example.bugit.model.GoogleSheetData

data class BugListScreenUiState (
    val loading: Boolean = false,
    val bugList: List<GoogleSheetData> = listOf()
)
