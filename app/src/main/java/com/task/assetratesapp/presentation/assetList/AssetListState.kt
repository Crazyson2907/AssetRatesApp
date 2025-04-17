package com.task.assetratesapp.presentation.assetList

import com.task.assetratesapp.domain.core.model.Asset

data class AssetListState(
    val isLoading: Boolean = false,// user-triggered actions
    val isRefreshing: Boolean = false,
    val assets: List<Asset> = emptyList(),
    val error: String? = null
)