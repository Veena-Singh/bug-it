package com.example.bugit.model

import com.google.gson.annotations.SerializedName

data class GoogleSheetData(
    @SerializedName("bug_description") val bugDescription: String?,
    @SerializedName("bug_image") val bugImage: String?
    )
