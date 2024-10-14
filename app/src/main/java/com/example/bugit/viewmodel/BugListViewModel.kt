package com.example.bugit.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bugit.model.repo.apiservice.GoogleApiService.getAllBugs
import com.example.bugit.view.uistate.BugListScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BugListViewModel: ViewModel()  {

    private val _uiState = MutableStateFlow(BugListScreenUiState())
    val uiState: StateFlow<BugListScreenUiState> = _uiState.asStateFlow()

    fun getAllBugList(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            handleLoading(true)
            try {
                val listOfBugs = getAllBugs(context)
                _uiState.update { currentState ->
                    currentState.copy(
                        bugList = listOfBugs,
                        loading = false
                    )
                }
            } catch (error: Exception) {
                handleLoading(isLoading = false)
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                loading = isLoading
            )
        }
    }
}