package com.example.rit.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class Network(
    private val interceptor: SupportInterceptor
): INetwork {

    override val retrofit: Retrofit by lazy {
        Retrofit.Builder()
        .baseUrl(Urls.RELEASE().DOG_URL)
        .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
        .client(buildClient())
        .build()
    }


    private fun buildClient() : OkHttpClient  {
        return  OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(EmptyBodyInterceptor)
//            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

}

