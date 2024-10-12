package com.example.bugit.viewmodel

import android.accounts.AccountManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bugit.R
import com.example.bugit.uistate.BugSubmissionScreenUiState
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.InputStreamReader
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
                        handleLoading(false)
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            val bugImageUrl = uri.toString()
                            Log.d(TAG, "Bug Image URL: $bugImageUrl")
                            saveToGoogleSheet(context, bugImageUrl, bugDescription) // Storing image url and description
                        }.addOnFailureListener { exception ->
                            Log.e(TAG, "Error getting Bug Image URL", exception)
                        }
                    }
                    .addOnFailureListener {
                        handleLoading(false)
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

    private fun getSheetsService(context: Context): Sheets {
        val httpTransport = com.google.api.client.http.javanet.NetHttpTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val inputStream: InputStream = context.resources.openRawResource(R.raw.credentials)
        // Load credentials from a file or environment variable
        val clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), InputStreamReader(inputStream))
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(SheetsScopes.SPREADSHEETS)
        )

        credential.selectedAccountName = clientSecrets.installed.clientId

        val accountName = credential.newChooseAccountIntent().getStringExtra(AccountManager.KEY_ACCOUNT_NAME)

        if (!accountName.isNullOrEmpty()) {
            credential.selectedAccountName = accountName
        } else {
            credential.selectedAccountName = "veenasinghits@gmail.com"
        }

        return Sheets.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName(applicationName)
            .build()
    }

    // Save image url and bug description on google sheet
    private fun saveToGoogleSheet(context: Context, bugImageUrl: String, bugDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val values = listOf(
                listOf(bugDescription, bugImageUrl)
            )
            val sheetsService = getSheetsService(context)
            val body = ValueRange().setValues(values)
            val result = sheetsService.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption(valueInputOption)
                .execute()
            Log.d(TAG, result.toString())
        }
    }

    companion object {
        const val TAG = "BugSubmissionViewModel"
        const val spreadsheetId = "1KaQkJ0KhXRza6bErF-yqW4qknddyDfwM-9D_EVC2tMI"
        const val range = "Sheet1!A1"
        const val valueInputOption = "RAW"
        const val applicationName = "Bug It"
    }
}