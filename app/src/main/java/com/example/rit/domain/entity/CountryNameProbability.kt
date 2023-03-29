package com.example.rit.domain.entity

import com.example.rit.data.dto.ApiCountryItem
import com.example.rit.data.dto.ApiCountryNameProbability

data class CountryNameProbability(
    val country: List<CountryItem> = emptyList(),
    val name: String = "",
)

data class CountryItem(
    val probability: Double = .0,
    val countryId: String = ""
)

fun ApiCountryItem.toCountryItem() = CountryItem(
    probability = probability ?: .0,
    countryId = countryId ?: ""
)

fun ApiCountryNameProbability.toCountryNameProbability() = CountryNameProbability(
    country = country?.map { it.toCountryItem() } ?: emptyList(),
    name = name ?: ""
)
