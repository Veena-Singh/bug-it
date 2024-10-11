package com.example.bugit.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.bugit.model.GoogleSheetData
import com.example.bugit.repo.ApiClient
import com.example.bugit.uistate.BugSubmissionScreenUiState
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class BugSubmissionViewModel: ViewModel()  {

    private val _uiState = MutableStateFlow(BugSubmissionScreenUiState())
    val uiState: StateFlow<BugSubmissionScreenUiState> = _uiState.asStateFlow()

    fun submitBug(imageUri: MutableState<Uri?>, context: Context) {
        handleLoading(true)
        val storageReference = FirebaseStorage.getInstance().reference
            .child(UUID.randomUUID().toString())
        imageUri.value?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {
                    handleLoading(false)
                    Toast.makeText(context, "File uploaded successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    handleLoading(false)
                    Toast.makeText(context, "File not uploaded", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun handleLoading(b: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                loading = true
            )
        }
    }

    fun saveToGoogleSheet() {
        val call = ApiClient.apiService.addBug(GoogleSheetData("", ""))

        call.enqueue(object : Callback<GoogleSheetData> {
            override fun onResponse(call: Call<GoogleSheetData>, response: Response<GoogleSheetData>) {
                if (response.isSuccessful) {
                    val post = response.body()
                    // Handle the retrieved post data
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<GoogleSheetData>, t: Throwable) {
                // Handle failure
            }
        })
    }
}