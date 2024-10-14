package com.example.bugit.model.repo

import android.accounts.AccountManager
import android.content.Context
import com.example.bugit.R
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import java.io.InputStream
import java.io.InputStreamReader

object GoogleSheetService {

    private const val applicationName = "Bug It"
    const val googleSheetId: String = "1KaQkJ0KhXRza6bErF-yqW4qknddyDfwM-9D_EVC2tMI"

    fun getGoogleSheetsService(context: Context): Sheets {
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

}