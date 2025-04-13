package com.task.assetratesapp.domain.core.usecase

import com.task.assetratesapp.domain.cache.usecase.GetAssetsFromCacheUseCase
import com.task.assetratesapp.domain.network.usecase.FetchAssetsFromApiUseCase

data class AssetUseCases(
    val fetchAssetsListUseCase: FetchAssetsListUseCase,
    val addAssetUseCase: AddAssetUseCase,
    val removeAssetUseCase: RemoveAssetUseCase,
    val getAssetsFromCacheUseCase: GetAssetsFromCacheUseCase,
    val fetchAssetsFromApiUseCase: FetchAssetsFromApiUseCase
)
