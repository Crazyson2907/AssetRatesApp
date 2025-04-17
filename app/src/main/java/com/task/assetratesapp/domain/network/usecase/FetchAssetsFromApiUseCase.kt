package com.task.assetratesapp.domain.network.usecase

import android.util.Log
import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.core.model.Asset
import com.task.assetratesapp.util.ExpiredApiKeyException
import com.task.assetratesapp.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchAssetsFromApiUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend fun execute(): Flow<Response<List<Asset>>> = flow {
        try {
            val assetCodes = repository.getAssets()
            val assetsWithRates = mutableListOf<Asset>()

            for (code in assetCodes) {
                val rate = repository.fetchRate(code)
                if (rate != null) {
                    assetsWithRates.add(Asset(code, rate))
                }
            }

            emit(Response.success(assetsWithRates))

        } catch (e: ExpiredApiKeyException) {
            emit(Response.error(e.message ?: "API key expired", null))
        } catch (e: Exception) {
            emit(Response.error("Unknown error", null))
        }
    }
}