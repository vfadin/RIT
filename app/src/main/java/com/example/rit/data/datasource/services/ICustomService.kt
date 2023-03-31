package com.example.rit.data.datasource.services

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface ICustomService {

    @GET
    suspend fun sendCustomRequest(@Url url: String): ResponseBody
}