package com.example.bugit.uistate

data class BugSubmissionScreenUiState(
    val loading: Boolean = false,
    val showImageFieldError: Boolean = false,
    val showDescriptionFieldError: Boolean = false
)
