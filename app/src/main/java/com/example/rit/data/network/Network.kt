package com.example.rit.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


object Network : INetwork {

    override val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Urls.DOG_URL)
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .client(buildClient())
            .build()
    }


    private fun buildClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(SupportInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(EmptyBodyInterceptor)
            .build()
    }

}

