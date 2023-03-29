package com.example.rit.data.datasource

import com.example.rit.data.dto.ApiCountryNameProbability
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface IBinListService {
    @GET
    suspend fun getNameInfo(
        @Url url: String,
        @Query("name[]") name: List<String>
    ): List<ApiCountryNameProbability>
}
