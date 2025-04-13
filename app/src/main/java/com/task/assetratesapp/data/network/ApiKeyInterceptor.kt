package com.task.assetratesapp.data.network

import com.task.assetratesapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the original request
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()

        // Append the API key as a query parameter
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("access_key", BuildConfig.EXCHANGE_RATE_API_KEY)
            .build()

        // Build a new request from the original one
        val newRequest = originalRequest.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }
}