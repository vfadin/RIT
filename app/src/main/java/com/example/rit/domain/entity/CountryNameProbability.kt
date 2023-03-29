package com.example.rit.domain.entity

import com.example.rit.data.dto.nationalize.ApiCountryItem
import com.example.rit.data.dto.nationalize.ApiCountryNameProbability
import java.util.*

data class CountryNameProbability(
    val country: List<CountryItem> = emptyList(),
    val name: String = "",
)

data class CountryItem(
    val probability: Double = .0,
    val countryId: String = "",
    val name: String = "",
) {
    override fun toString(): String {
        return "$name $countryId = $probability\n"
    }
}

fun ApiCountryItem.toCountryItem() = CountryItem(
    probability = probability ?: .0,
    countryId = countryId ?: "",
    name = countryId?.let { Locale("", it).displayCountry } ?: ""
)

fun ApiCountryNameProbability.toCountryNameProbability() = CountryNameProbability(
    country = country?.map { it.toCountryItem() } ?: emptyList(),
    name = name ?: ""
)
