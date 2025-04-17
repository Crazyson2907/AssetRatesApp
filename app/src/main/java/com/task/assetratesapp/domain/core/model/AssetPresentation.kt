package com.task.assetratesapp.domain.core.model

data class AssetPresentation(
    val code: String,
    val displayRate: String
)

fun Asset.toPresentation(): AssetPresentation {
    return AssetPresentation(
        code = this.code,
        displayRate = String.format("%.6f", this.rate),
    )
}