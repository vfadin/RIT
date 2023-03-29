package com.example.rit.domain.repo

import com.example.rit.domain.RequestResult
import com.example.rit.domain.entity.CountryNameProbability

interface IHomeRepo {
    suspend fun getNameInfo(name: List<String>): RequestResult<List<CountryNameProbability>>
}