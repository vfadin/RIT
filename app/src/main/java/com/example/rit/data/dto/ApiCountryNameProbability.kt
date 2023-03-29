package com.example.rit.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiCountryNameProbability(
    @Json(name = "country")
    val country: List<ApiCountryItem>?,
    @Json(name = "name")
    val name: String?,
)

@JsonClass(generateAdapter = true)
data class ApiCountryItem(
    @Json(name = "probability")
    val probability: Double?,
    @Json(name = "country_id")
    val countryId: String?
)