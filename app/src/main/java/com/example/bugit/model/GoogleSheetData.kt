package com.example.bugit.model

data class GoogleSheetData(
    val googleSheetName: String,
    val googleSheetData: List<MutableList<Any>>
    )
