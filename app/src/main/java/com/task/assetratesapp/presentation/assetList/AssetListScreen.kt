package com.task.assetratesapp.presentation.assetList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.task.assetratesapp.presentation.assetList.components.AssetListItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetListScreen(
    viewModel: AssetViewModel,
    onNavigateToAddAsset: () -> Unit
) {
    val assetListState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assets") },
                actions = {
                    IconButton(onClick = onNavigateToAddAsset) {
                        Text("+")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                assetListState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                assetListState.error != null -> {
                    Text(
                        text = assetListState.error ?: "An unknown error occurred",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(assetListState.assets) { asset ->
                            AssetListItem(
                                asset = asset,
                                onRemove = { viewModel.handleIntent(AssetListIntent.RemoveAsset(asset.code)) }
                            )
                        }
                    }
                }
            }
        }
    }
}