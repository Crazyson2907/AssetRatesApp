package com.task.assetratesapp.domain.network.usecase

import com.task.assetratesapp.domain.cache.core.AssetRepository
import javax.inject.Inject

class GetSupportedCurrenciesUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend fun execute(): Map<String, String> {
        return repository.getSupportedCurrencies()
    }
}