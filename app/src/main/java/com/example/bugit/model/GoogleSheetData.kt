package com.example.bugit.model

data class GoogleSheetData(
    val sheetName: String,
    val sheetData: List<MutableList<Any>>
    )
