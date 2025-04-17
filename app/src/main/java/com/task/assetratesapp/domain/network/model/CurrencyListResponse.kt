package com.task.assetratesapp.domain.network.model

data class CurrencyListResponse(
    val success: Boolean,
    val terms: String?,
    val privacy: String?,
    val currencies: Map<String, String> // "USD" -> "United States Dollar"
)