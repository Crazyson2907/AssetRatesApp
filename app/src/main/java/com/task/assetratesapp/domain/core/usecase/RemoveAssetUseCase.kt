package com.task.assetratesapp.domain.core.usecase

import com.task.assetratesapp.domain.cache.core.AssetRepository

class RemoveAssetUseCase(private val repository: AssetRepository) {
    suspend operator fun invoke(assetCode: String) {
        repository.removeAsset(assetCode)
    }
}