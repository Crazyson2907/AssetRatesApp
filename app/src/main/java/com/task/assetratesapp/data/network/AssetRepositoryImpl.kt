package com.task.assetratesapp.data.network

import android.content.Context
import com.task.assetratesapp.data.cache.AssetDatabase
import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.cache.entities.AssetEntity
import com.task.assetratesapp.domain.network.core.ExchangeRatesApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssetRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ExchangeRatesApiService,
    private val database: AssetDatabase
) : AssetRepository {

    private val assetDao = database.assetDao()

    companion object {
        val DEFAULT_ASSETS = listOf("USD", "EUR", "BTC")
    }

    override suspend fun getAssets(): List<String> = withContext(Dispatchers.IO) {
        val assetEntities = assetDao.getAssets()
        if (assetEntities.isEmpty()) {
            DEFAULT_ASSETS.forEach { code ->
                assetDao.addAsset(AssetEntity(code))
            }
            DEFAULT_ASSETS
        } else {
            assetEntities.map { it.code }
        }
    }

    override suspend fun addAsset(code: String) = withContext(Dispatchers.IO) {
        assetDao.addAsset(AssetEntity(code))
    }

    override suspend fun removeAsset(code: String) = withContext(Dispatchers.IO) {
        assetDao.deleteAsset(AssetEntity(code))
    }

    override suspend fun fetchRate(assetCode: String): Double? {
        return try {
            // The ExchangeRate.host API returns quotes with keys like "USD<assetCode>"
            val response = api.getLatestRates(base = "USD", symbols = assetCode)
            response.quotes["USD$assetCode"]
        } catch (e: Exception) {
            null
        }
    }
}