package com.example.rit.di

import com.example.rit.data.datasource.services.ICustomService
import com.example.rit.data.datasource.services.IDogService
import com.example.rit.data.datasource.RemoteDataSource
import com.example.rit.data.datasource.services.INationalizeService
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
    fun provideNetwork(): INetwork {
        return Network
    }

    @Provides
    fun provideNationalizeService(network: INetwork): INationalizeService {
        return network.retrofit.create(INationalizeService::class.java)
    }

    @Provides
    fun provideDogService(network: INetwork): IDogService {
        return network.retrofit.create(IDogService::class.java)
    }

    @Provides
    fun provideCustomService(network: INetwork): ICustomService {
        return network.retrofit.create(ICustomService::class.java)
    }

}

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataSourceModule {

    @ActivityRetainedScoped
    fun provideBinListRemoteDataSource(api: INationalizeService): RemoteDataSource {
        return provideBinListRemoteDataSource(api)
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideHomeRepo(
        dataSource: RemoteDataSource,
    ): IHomeRepo {
        return HomeRepo(dataSource)
    }
}