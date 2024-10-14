package com.example.bugit.model.repo.apiservice

import android.content.Context
import com.example.bugit.model.GoogleSheetData
import com.example.bugit.model.repo.GoogleSheetService
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.AddSheetRequest
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import com.google.api.services.sheets.v4.model.Request
import com.google.api.services.sheets.v4.model.SheetProperties
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import java.util.Collections


object GoogleApiService {

    private const val range = "!A1"
    private const val valueInputOption = "RAW"

    // Save bug data in google sheet
    fun saveBugDataInGoogleSheet(sheetsService: Sheets, currentDate: String, bugData: ValueRange): UpdateValuesResponse? {
        if (isSheetExists(sheetsService, currentDate).not()) {
            val addSheetRequest =
                AddSheetRequest().setProperties(SheetProperties().setTitle(currentDate))
            val batchUpdateRequest = BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(Request().setAddSheet(addSheetRequest)))
            sheetsService.spreadsheets().batchUpdate(GoogleSheetService.googleSheetId, batchUpdateRequest)
                .execute()
        }
        val result = sheetsService.spreadsheets().values()
            .append(GoogleSheetService.googleSheetId, currentDate + range, bugData)
            .setValueInputOption(valueInputOption)
            .execute()

        return result?.updates
    }

    // Get all created tabs/sheet names
    private fun getSheetNames(context: Context): List<String> {
        val spreadsheet: Spreadsheet = GoogleSheetService.getGoogleSheetsService(context).spreadsheets().get(
            GoogleSheetService.googleSheetId
        ).execute()
        return spreadsheet.sheets.map { it.properties.title }
    }

    fun getAllBugs(context: Context): List<GoogleSheetData> {
        val sheetNames = getSheetNames(context)
        val listOfBugs = mutableListOf<GoogleSheetData>()
        sheetNames.forEach { sheetName ->
            val sheetData = getSheetData(sheetName, context)
            listOfBugs.add(sheetData)
        }
        return listOfBugs
    }

    // Get all list of bugs
    private fun getSheetData(sheetName: String, context: Context): GoogleSheetData {
        val response: ValueRange = GoogleSheetService.getGoogleSheetsService(context).spreadsheets().values().get(
            GoogleSheetService.googleSheetId, sheetName).execute()
        val data = response.getValues() ?: listOf()
        return GoogleSheetData(sheetName, data)
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
}