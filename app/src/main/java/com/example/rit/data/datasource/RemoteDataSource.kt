package com.example.rit.data.datasource

import com.example.rit.data.datasource.services.ICustomService
import com.example.rit.data.datasource.services.IDogService
import com.example.rit.data.datasource.services.INationalizeService
import com.example.rit.data.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiNationalize: INationalizeService,
    private val apiCustom: ICustomService,
    private val apiDog: IDogService
) {
    suspend fun getNationalizeInfoByName(url: String, name: List<String>) = safeApiCall {
        apiNationalize.getNameInfo(url, name)
    }

    suspend fun getDogImage() = safeApiCall {
        apiDog.getImage()
    }

    suspend fun sendCustomRequest(url: String) = safeApiCall {
        apiCustom.sendCustomRequest(url)
    }
}