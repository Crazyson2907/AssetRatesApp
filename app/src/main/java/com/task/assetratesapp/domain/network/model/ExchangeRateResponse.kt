package com.task.assetratesapp.domain.network.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val source: String,
    // The API returns rates under the "quotes" field.
    @SerializedName("quotes") val quotes: Map<String, Double>
)