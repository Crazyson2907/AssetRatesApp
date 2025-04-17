package com.task.assetratesapp.domain.core.usecase

import com.task.assetratesapp.domain.cache.usecase.GetAssetsFromCacheUseCase
import com.task.assetratesapp.domain.network.usecase.FetchAssetsFromApiUseCase
import com.task.assetratesapp.domain.network.usecase.GetSupportedCurrenciesUseCase
import javax.inject.Inject

data class AssetUseCases @Inject constructor(
    val fetchAssetsListUseCase: FetchAssetsListUseCase,
    val addAssetUseCase: AddAssetUseCase,
    val removeAssetUseCase: RemoveAssetUseCase,
    val getAssetsFromCacheUseCase: GetAssetsFromCacheUseCase,
    val fetchAssetsFromApiUseCase: FetchAssetsFromApiUseCase,
    val initializeAssetsUseCase: InitializeAssetsUseCase,
    val getSupportedCurrenciesUseCase: GetSupportedCurrenciesUseCase
)
