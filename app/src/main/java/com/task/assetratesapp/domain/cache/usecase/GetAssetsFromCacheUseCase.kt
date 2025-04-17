package com.task.assetratesapp.domain.cache.usecase

import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.core.model.Asset
import javax.inject.Inject

class GetAssetsFromCacheUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend fun execute(): List<Asset> {
        val codes = repository.getAssets()
        return codes.map { Asset(it, rate = 0.0) }
    }
}