package com.example.bugit.view.uistate

data class BugSubmissionScreenUiState(
    val loading: Boolean = false,
    val showImageFieldError: Boolean = false,
    val showDescriptionFieldError: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showFailureDialog: Boolean = false
)
