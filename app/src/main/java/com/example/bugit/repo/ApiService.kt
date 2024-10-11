package com.example.bugit.repo

import com.example.bugit.model.GoogleSheetData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("bug")
    fun addBug(@Body sheetData: GoogleSheetData): Call<GoogleSheetData>
}