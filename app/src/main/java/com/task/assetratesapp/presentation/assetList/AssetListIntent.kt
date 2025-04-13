package com.task.assetratesapp.presentation.assetList

sealed class AssetListIntent {
    object LoadAssets : AssetListIntent()
    data class AddAsset(val assetCode: String) : AssetListIntent()
    data class RemoveAsset(val assetCode: String) : AssetListIntent()
    object RefreshRates : AssetListIntent()
}