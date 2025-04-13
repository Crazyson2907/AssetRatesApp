package com.task.assetratesapp.domain.network.core

import com.task.assetratesapp.domain.network.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesApiService {
    @GET("live")
    suspend fun getLatestRates(
        @Query("base") base: String = "USD",
        @Query("symbols") symbols: String
    ): ExchangeRateResponse
}