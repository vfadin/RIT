package com.example.rit.data.datasource.services

import com.example.rit.data.dto.dog.ApiDogImage
import retrofit2.http.GET

interface IDogService {

    @GET("api/breeds/image/random")
    suspend fun getImage(): ApiDogImage
}