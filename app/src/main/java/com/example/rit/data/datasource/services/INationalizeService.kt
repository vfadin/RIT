package com.example.rit.data.datasource.services

import com.example.rit.data.dto.nationalize.ApiCountryNameProbability
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface INationalizeService {

    @GET
    suspend fun getNameInfo(
        @Url url: String,
        @Query("name[]") name: List<String>,
    ): List<ApiCountryNameProbability>
}
