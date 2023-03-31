package com.example.rit.data.repo

import com.example.rit.data.datasource.RemoteDataSource
import com.example.rit.data.network.Urls
import com.example.rit.domain.RequestResult
import com.example.rit.domain.entity.CountryNameProbability
import com.example.rit.domain.entity.toCountryNameProbability
import com.example.rit.domain.repo.IHomeRepo

class HomeRepo(
    private val dataSource: RemoteDataSource,
) : IHomeRepo {

    override suspend fun getNameInfo(name: List<String>): RequestResult<List<CountryNameProbability>> {
        return when (val response =
            dataSource.getNationalizeInfoByName(Urls.NATIONALIZE_URL, name)) {
            is RequestResult.Success ->
                RequestResult.Success(response.result.map { it.toCountryNameProbability() })
            is RequestResult.Error -> RequestResult.Error(response.exception)
        }
    }

    override suspend fun getDogImage(): RequestResult<String> {
        return when (val response = dataSource.getDogImage()) {
            is RequestResult.Success -> RequestResult.Success(response.result.url ?: "")
            is RequestResult.Error -> RequestResult.Error(response.exception)
        }
    }

    override suspend fun sendCustomRequest(url: String): RequestResult<String> {
        return when (val response = dataSource.sendCustomRequest(url)) {
            is RequestResult.Success -> RequestResult.Success(response.result.string())
            is RequestResult.Error -> {
                RequestResult.Error(response.exception, response.data)
            }
        }
    }
}
