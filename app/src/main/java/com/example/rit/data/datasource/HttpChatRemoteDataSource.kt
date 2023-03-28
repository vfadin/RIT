package com.example.rit.data.datasource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BinListRemoteDataSource @Inject constructor(
    private val api: IBinListService
) {

}