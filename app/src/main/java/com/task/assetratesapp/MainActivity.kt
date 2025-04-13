package com.task.assetratesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.assetratesapp.presentation.addAssets.AddAssetScreen
import com.task.assetratesapp.presentation.assetList.AssetListIntent
import com.task.assetratesapp.presentation.assetList.AssetListScreen
import com.task.assetratesapp.presentation.assetList.AssetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: AssetViewModel = hiltViewModel()

            // A simple screen state to toggle between Home and Add screens.
            var currentScreen by remember { mutableStateOf("home") }

            // Simple navigation
            when (currentScreen) {
                "home" -> {
                    AssetListScreen(
                        viewModel = viewModel,
                        onNavigateToAddAsset = { currentScreen = "add" }
                    )
                }
                "add" -> {
                    AddAssetScreen(
                        onAssetSelected = { assetCode ->
                            viewModel.handleIntent(AssetListIntent.AddAsset(assetCode))
                            currentScreen = "home"
                        },
                        onBack = { currentScreen = "home" }
                    )
                }
            }
        }
    }
}