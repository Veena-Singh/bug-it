package com.example.bugit.model

data class GoogleSheetData(
    val sheetName: String,
    val data: List<MutableList<Any>>
    )
