package com.example.rit.domain.repo

import com.example.rit.domain.RequestResult
import com.example.rit.domain.entity.CountryNameProbability

interface IHomeRepo {
    suspend fun getNameInfo(name: String): RequestResult<CountryNameProbability>
}