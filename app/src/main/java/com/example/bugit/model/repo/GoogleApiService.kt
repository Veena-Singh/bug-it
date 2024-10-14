package com.example.bugit.model.repo

import android.accounts.AccountManager
import android.content.Context
import com.example.bugit.R
import com.example.bugit.common.util.Constant.applicationName
import com.example.bugit.common.util.Constant.googleSheetId
import com.example.bugit.common.util.Constant.range
import com.example.bugit.common.util.Constant.valueInputOption
import com.example.bugit.model.GoogleSheetData
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.AddSheetRequest
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import com.google.api.services.sheets.v4.model.Request
import com.google.api.services.sheets.v4.model.SheetProperties
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Collections


object GoogleApiService {

    private fun getGoogleSheetsService(context: Context): Sheets {
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

    // Save bug data in google sheet
    fun saveBugDataInGoogleSheet(context: Context, currentDate: String, bugData: ValueRange): UpdateValuesResponse? {
        val sheetService = getGoogleSheetsService(context)
        if (isSheetExists(sheetService, currentDate).not()) {
            val addSheetRequest =
                AddSheetRequest().setProperties(SheetProperties().setTitle(currentDate))
            val batchUpdateRequest = BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(Request().setAddSheet(addSheetRequest)))
            sheetService.spreadsheets().batchUpdate(googleSheetId, batchUpdateRequest)
                .execute()
        }
        val result = sheetService.spreadsheets().values()
            .append(googleSheetId, currentDate + range, bugData)
            .setValueInputOption(valueInputOption)
            .execute()

        return result?.updates
    }

    // Get all created tabs/sheet names
    private fun getSheetNames(context: Context): List<String> {
        val spreadsheet: Spreadsheet = getGoogleSheetsService(context).spreadsheets().get(
            googleSheetId
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
        val response: ValueRange = getGoogleSheetsService(context).spreadsheets().values().get(
            googleSheetId, sheetName).execute()
        val data = response.getValues() ?: listOf()
        return GoogleSheetData(sheetName, data)
    }

    private fun isSheetExists(service: Sheets, sheetName: String): Boolean {
        val spreadsheet: Spreadsheet = service.spreadsheets().get(googleSheetId).execute()
        val sheets = spreadsheet.sheets
        for (sheet in sheets) {
            if (sheet.properties.title == sheetName) {
                return true
            }
        }
        return false
    }
}