package com.task.assetratesapp.data.network

import android.content.ContentValues.TAG
import android.util.Log
import com.task.assetratesapp.data.cache.AssetDatabase
import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.cache.entities.AssetEntity
import com.task.assetratesapp.domain.network.core.ExchangeRatesApiService
import com.task.assetratesapp.util.ExpiredApiKeyException
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
        try {
            val response = api.getLatestRates(base = "USD", symbols = assetCode)

            if (!response.success) {
                Log.e(TAG, "API Error: ${response.error?.type} - ${response.error?.info}")

                if (response.error?.code == 101 || response.error?.code == 104) {
                    throw ExpiredApiKeyException(response.error?.info ?: "API key expired")
                }

                return@withContext null
            }

            val key = "USD$assetCode"
            return@withContext response.quotes?.get(key)
        } catch (e: ExpiredApiKeyException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "General error fetching rate for $assetCode", e)
            return@withContext null
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