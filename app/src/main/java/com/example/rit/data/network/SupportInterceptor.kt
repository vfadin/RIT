package com.example.rit.data.network

import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().apply {
            if (request.header("Content-Type") == null) {
                header("Content-Type", "application/json")
            }
        }.build()
        return chain.proceed(request)
    }
}