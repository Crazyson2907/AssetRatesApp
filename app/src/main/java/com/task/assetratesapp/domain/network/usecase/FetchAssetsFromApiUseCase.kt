package com.task.assetratesapp.domain.network.usecase

import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.core.model.Asset
import com.task.assetratesapp.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchAssetsFromApiUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend fun execute(): Flow<Response<List<Asset>>> = flow {
        val assetCodes = repository.getAssets()
        val assetsWithRates = assetCodes.mapNotNull { code ->
            val rate = repository.fetchRate(code)
            rate?.let { Asset(code, it) }
        }
        emit(Response.success(assetsWithRates))
    }
}