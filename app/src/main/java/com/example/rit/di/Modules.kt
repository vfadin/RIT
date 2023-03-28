package com.example.rit.di

import com.example.rit.data.datasource.BinListRemoteDataSource
import com.example.rit.data.datasource.IBinListService
import com.example.rit.data.network.INetwork
import com.example.rit.data.network.Network
import com.example.rit.data.network.SupportInterceptor
import com.example.rit.data.repo.HomeRepo
import com.example.rit.domain.repo.IHomeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideSupportInterceptor(): SupportInterceptor {
        return SupportInterceptor()
    }

    @Provides
    fun provideNetwork(supportInterceptor: SupportInterceptor): INetwork {
        return Network(supportInterceptor)
    }

    @Provides
    fun provideBinListService(network: INetwork): IBinListService {
        return network.retrofit.create(
            IBinListService::class.java
        )
    }

}

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataSourceModule {

    @ActivityRetainedScoped
    fun provideBinListRemoteDataSource(api: IBinListService): BinListRemoteDataSource {
        return provideBinListRemoteDataSource(api)
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideHomeRepo(
        dataSource: BinListRemoteDataSource,
    ): IHomeRepo {
        return HomeRepo(dataSource)
    }
}