package com.task.assetratesapp.domain.core.usecase

import android.content.Context
import com.task.assetratesapp.domain.cache.core.AssetRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class InitializeAssetsUseCase @Inject constructor(
    private val repository: AssetRepository,
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("asset_prefs", Context.MODE_PRIVATE)
    private val KEY_INIT = "assets_initialized"

    suspend fun execute() {
        if (!prefs.getBoolean(KEY_INIT, false)) {
            DEFAULT_ASSETS.forEach { code ->
                repository.addAsset(code)
            }
            prefs.edit() { putBoolean(KEY_INIT, true) }
        }
    }

    companion object {
        val DEFAULT_ASSETS = listOf("USD", "EUR", "BTC")
    }
}