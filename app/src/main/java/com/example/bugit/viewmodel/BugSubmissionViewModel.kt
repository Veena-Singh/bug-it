package com.example.bugit.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bugit.repo.GoogleSheetService
import com.example.bugit.uistate.BugSubmissionScreenUiState
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.AddSheetRequest
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import com.google.api.services.sheets.v4.model.Request
import com.google.api.services.sheets.v4.model.SheetProperties
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.UUID

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
            val storageReference = FirebaseStorage.getInstance().reference
                .child(UUID.randomUUID().toString())
            imageUri.let {
                storageReference.putFile(it)
                    .addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            val bugImageUrl = uri.toString()
                            Log.d(TAG, "Bug Image URL: $bugImageUrl")
                            saveToGoogleSheet(context, bugImageUrl, bugDescription) // Storing image url and description
                        }.addOnFailureListener { exception ->
                            handleLoading(false)
                            handleDialog(showSuccessDialog = false, showFailureDialog = true)
                            Log.e(TAG, "Error getting Bug Image URL", exception)
                        }
                    }
                    .addOnFailureListener {
                        handleLoading(false)
                        handleDialog(showSuccessDialog = false, showFailureDialog = true)
                    }
            }
        }
    }

     fun showImageFieldError(imageUri: Uri?) {
        _uiState.update { currentState ->
            currentState.copy(
                showImageFieldError = imageUri == null,
            )
        }
    }

    fun showBugDescriptionFieldError(bugDescription: String) {
        _uiState.update { currentState ->
            currentState.copy(
                showDescriptionFieldError = bugDescription == "",
            )
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                loading = isLoading
            )
        }
    }

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
            val sheetsService = GoogleSheetService.getSheetsService(context)
            val body = ValueRange().setValues(values)
            val currentDate = getCurrentDate()
            if (isSheetExists(sheetsService, currentDate).not()) {
                val addSheetRequest =
                    AddSheetRequest().setProperties(SheetProperties().setTitle(currentDate))
                val batchUpdateRequest = BatchUpdateSpreadsheetRequest()
                    .setRequests(Collections.singletonList(Request().setAddSheet(addSheetRequest)))
                sheetsService.spreadsheets().batchUpdate(GoogleSheetService.googleSheetId, batchUpdateRequest)
                    .execute()
            }
            val result = sheetsService.spreadsheets().values()
                .append(GoogleSheetService.googleSheetId, currentDate + range, body)
                .setValueInputOption(valueInputOption)
                .execute()
            Log.d(TAG, result.toString())
            if (result?.updates != null) {
                handleLoading(false)
                handleDialog(showSuccessDialog = true, showFailureDialog = false)
            } else {
                handleLoading(false)
                handleDialog(showSuccessDialog = false, showFailureDialog = true)
            }
        }
    }

    private fun isSheetExists(service: Sheets, sheetName: String): Boolean {
        val spreadsheet: Spreadsheet = service.spreadsheets().get(GoogleSheetService.googleSheetId).execute()
        val sheets = spreadsheet.sheets
        for (sheet in sheets) {
            if (sheet.properties.title == sheetName) {
                return true
            }
        }
        return false
    }

    private fun getCurrentDate(): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).formatDate()
    }

    private fun String.formatDate(): String {
        val parts = this.split("/")
        return if (parts.size == 3) {
            val (day, month, year) = parts
            "$day-$month-$year"
        } else {
            this // Return the original string if it doesn't match the expected format
        }
    }

    companion object {
        const val TAG = "BugSubmissionViewModel"
        const val range = "!A1"
        const val valueInputOption = "RAW"
    }
}