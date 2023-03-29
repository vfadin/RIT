package com.example.rit.data.dto.dog

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDogImage(
    @Json(name = "message")
    val url: String?,
    @Json(name = "status")
    val status: String?
)
