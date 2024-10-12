package com.example.bugit.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bugit.model.GoogleSheetData
import com.example.bugit.repo.GoogleSheetService
import com.example.bugit.uistate.BugListScreenUiState
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.ValueRange
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
            val sheetNames = getSheetNames(context)
            val listOfBugs = mutableListOf<GoogleSheetData>()
            sheetNames.forEach { sheetName ->
                val sheetData =  getSheetData(sheetName, context)
                listOfBugs.add(sheetData)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    bugList = listOfBugs,
                    loading = false
                )
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

    private fun getSheetNames(context: Context): List<String> {
        val spreadsheet: Spreadsheet = GoogleSheetService.getSheetsService(context).spreadsheets().get(GoogleSheetService.googleSheetId).execute()
        return spreadsheet.sheets.map { it.properties.title }
    }

    private fun getSheetData(sheetName: String, context: Context): GoogleSheetData {
        val response: ValueRange = GoogleSheetService.getSheetsService(context).spreadsheets().values().get(GoogleSheetService.googleSheetId, sheetName).execute()
        val data = response.getValues() ?: listOf()
        return GoogleSheetData(sheetName, data)
    }
}