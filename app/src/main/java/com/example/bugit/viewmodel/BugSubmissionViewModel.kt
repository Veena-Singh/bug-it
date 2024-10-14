package com.example.bugit.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bugit.common.util.DateUtils
import com.example.bugit.model.repo.GoogleApiService
import com.example.bugit.model.repo.FirebaseApiService
import com.example.bugit.view.uistate.BugSubmissionScreenUiState
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BugSubmissionViewModel: ViewModel()  {

    private val _uiState = MutableStateFlow(BugSubmissionScreenUiState())
    val uiState: StateFlow<BugSubmissionScreenUiState> = _uiState.asStateFlow()

    // Save Image on Firebase to get url for storing on google sheet
    fun submitBug(
        imageUri: Uri?,
        bugDescription: String,
        context: Context
    ) {
        if (imageUri == null || bugDescription == "") {
            showImageFieldError(imageUri)
            showBugDescriptionFieldError(bugDescription)
            return
        }
        handleLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            imageUri.let {
                FirebaseApiService.uploadImageToGetImageLink(imageUri,
                    onSuccess = { imageUrlFromFirebase ->
                        imageUrlFromFirebase?.let {
                                url -> saveToGoogleSheet(context, url, bugDescription) }
                    },
                    onFailure = {
                        handleLoading(false)
                        handleDialog(showSuccessDialog = false, showFailureDialog = true)
                    })
            }
        }
    }

    // If image not provided for bug creation show error
     fun showImageFieldError(imageUri: Uri?) {
        _uiState.update { currentState ->
            currentState.copy(
                showImageFieldError = imageUri == null,
            )
        }
    }

    // If description not provided for bug creation show error
    fun showBugDescriptionFieldError(bugDescription: String) {
        _uiState.update { currentState ->
            currentState.copy(
                showDescriptionFieldError = bugDescription == "",
            )
        }
    }

    // While submitting bug/ network call show loading on screen
    private fun handleLoading(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                loading = isLoading
            )
        }
    }

    // After network call show success or error dialog
     fun handleDialog(showSuccessDialog: Boolean = false, showFailureDialog: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                showFailureDialog = showFailureDialog,
                showSuccessDialog = showSuccessDialog
            )
        }
    }

    // Save image url and bug description on google sheet
    private fun saveToGoogleSheet(context: Context, bugImageUrl: String, bugDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val values = listOf(
                listOf(bugDescription, bugImageUrl)
            )
            val bugData = ValueRange().setValues(values)
            val currentDate = DateUtils.getCurrentDate()
            val result = GoogleApiService.saveBugDataInGoogleSheet(context, currentDate, bugData)
            if (result != null) {
                handleLoading(false)
                handleDialog(showSuccessDialog = true, showFailureDialog = false)
            } else {
                handleLoading(false)
                handleDialog(showSuccessDialog = false, showFailureDialog = true)
            }
        }
    }
}