package com.task.assetratesapp.data.network

import android.content.ContentValues.TAG
import android.util.Log
import com.task.assetratesapp.data.cache.AssetDatabase
import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.cache.entities.AssetEntity
import com.task.assetratesapp.domain.network.core.ExchangeRatesApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssetRepositoryImpl @Inject constructor(
    private val api: ExchangeRatesApiService,
    private val database: AssetDatabase
) : AssetRepository {

    override suspend fun getAssets(): List<String> = withContext(Dispatchers.IO) {
        database.assetDao().getAssets().map { it.code }
    }

    override suspend fun addAsset(code: String) = withContext(Dispatchers.IO) {
        assetDao.addAsset(AssetEntity(code))
    }

    override suspend fun removeAsset(code: String) = withContext(Dispatchers.IO) {
        assetDao.deleteAsset(AssetEntity(code))
    }

    override suspend fun fetchRate(assetCode: String): Double? = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching rate for asset: $assetCode")
        return@withContext try {
            // The key for the quote should be "USD<assetCode>"
            val response = api.getLatestRates(base = "USD", symbols = assetCode)
            val key = "USD$assetCode"
            val rate = response.quotes[key]
            Log.d(TAG, "Received rate for $assetCode: $rate")
            rate
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching rate for $assetCode", e)
            null
        }
    }

    override suspend fun getSupportedCurrencies(): Map<String, String> = withContext(Dispatchers.IO) {
        try {
            val response = api.getSupportedCurrencies()
            if (response.success && response.currencies != null) {
                response.currencies
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            Log.e("AssetRepo", "Failed to fetch supported currencies", e)
            emptyMap()
        }
    }

    private val assetDao = database.assetDao()
}