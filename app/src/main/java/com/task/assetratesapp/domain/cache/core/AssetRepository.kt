package com.task.assetratesapp.domain.cache.core

interface AssetRepository {
    suspend fun getAssets(): List<String>
    suspend fun addAsset(code: String)
    suspend fun removeAsset(code: String)
    suspend fun fetchRate(assetCode: String): Double?
}