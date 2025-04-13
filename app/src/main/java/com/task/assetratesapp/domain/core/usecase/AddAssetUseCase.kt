package com.task.assetratesapp.domain.core.usecase

import com.task.assetratesapp.domain.cache.core.AssetRepository

class AddAssetUseCase(private val repository: AssetRepository) {
    suspend operator fun invoke(assetCode: String) {
        repository.addAsset(assetCode)
    }
}