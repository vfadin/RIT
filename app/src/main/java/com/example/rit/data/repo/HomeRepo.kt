package com.example.rit.data.repo

import com.example.rit.data.datasource.BinListRemoteDataSource
import com.example.rit.domain.repo.IHomeRepo

class HomeRepo(
    private val dataSource: BinListRemoteDataSource
) : IHomeRepo {

}
