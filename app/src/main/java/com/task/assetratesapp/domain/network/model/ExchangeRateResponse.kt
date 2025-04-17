package com.task.assetratesapp.domain.network.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val source: String,
    val error: ApiError? = null,
    // The API returns rates under the "quotes" field.
    @SerializedName("quotes") val quotes: Map<String, Double>
)

data class ApiError(
    val code: Int,
    val type: String,
    val info: String
)