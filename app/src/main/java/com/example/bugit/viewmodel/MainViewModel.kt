package com.example.bugit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bugit.uistate.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class MainViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun setImageUri(uri: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                imageUri = uri
            )
        }
    }
}