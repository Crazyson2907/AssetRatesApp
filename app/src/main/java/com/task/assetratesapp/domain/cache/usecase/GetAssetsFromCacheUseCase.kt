package com.task.assetratesapp.domain.cache.usecase

import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.core.model.Asset

class GetAssetsFromCacheUseCase(private val repository: AssetRepository) {
    suspend fun execute(): List<Asset> {
        // repository.getAssets() returns List<String>; map them to Asset.
        return repository.getAssets().map { code ->
            // If you don’t cache the rate, use 0.0 (or any placeholder) until updated.
            Asset(code, 0.0)
        }
    }
}