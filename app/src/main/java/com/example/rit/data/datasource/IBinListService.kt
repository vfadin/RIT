package com.example.rit.data.datasource

import com.example.rit.data.dto.dog.ApiDogImage
import com.example.rit.data.dto.nationalize.ApiCountryNameProbability
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IBinListService {
    @GET
    suspend fun getNameInfo(
        @Url url: String,
        @Query("name[]") name: List<String>,
    ): List<ApiCountryNameProbability>

    @GET("api/breeds/image/random")
    suspend fun getImage(): ApiDogImage

    @GET
    suspend fun sendCustomRequest(@Url url: String): ResponseBody
}
