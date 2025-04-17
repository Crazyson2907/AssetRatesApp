package com.task.assetratesapp.domain.core.usecase

import com.task.assetratesapp.domain.cache.core.AssetRepository
import javax.inject.Inject

class AddAssetUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(code: String) {
        repository.addAsset(code)
    }
}