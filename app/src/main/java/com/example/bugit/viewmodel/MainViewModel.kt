package com.example.bugit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bugit.view.uistate.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // Handle the state of bug image when sharing from gallery and launching BugSubmissionScreen
    fun setImageUri(uri: String) {
        _uiState.update { currentState ->
            currentState.copy(
                imageUri = uri
            )
        }
    }
}