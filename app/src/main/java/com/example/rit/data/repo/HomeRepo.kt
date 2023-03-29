package com.example.rit.data.repo

import com.example.rit.data.datasource.BinListRemoteDataSource
import com.example.rit.domain.RequestResult
import com.example.rit.domain.entity.CountryNameProbability
import com.example.rit.domain.entity.toCountryNameProbability
import com.example.rit.domain.repo.IHomeRepo

class HomeRepo(
    private val dataSource: BinListRemoteDataSource,
) : IHomeRepo {

    override suspend fun getNameInfo(name: String): RequestResult<CountryNameProbability> {
        return when (val response = dataSource.getNationalizeInfoByName("https://api.nationalize.io/", name)) {
            is RequestResult.Success -> RequestResult.Success(response.result.toCountryNameProbability())
            is RequestResult.Error -> RequestResult.Error(response.exception)
        }
    }
}
