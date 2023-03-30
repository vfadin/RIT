package com.example.rit.data.datasource

import com.example.rit.data.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val api: IBinListService,
) {
    suspend fun getNationalizeInfoByName(url: String, name: List<String>) = safeApiCall {
        api.getNameInfo(url, name)
    }

    suspend fun getDogImage() = safeApiCall {
        api.getImage()
    }

    suspend fun sendCustomRequest(url: String) = safeApiCall {
        api.sendCustomRequest(url)
    }
}