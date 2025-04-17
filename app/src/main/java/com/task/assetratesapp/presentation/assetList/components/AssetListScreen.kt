package com.task.assetratesapp.presentation.assetList.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.task.assetratesapp.domain.core.model.toPresentation
import com.task.assetratesapp.presentation.assetList.AssetListIntent
import com.task.assetratesapp.presentation.assetList.AssetViewModel


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
                title = { Text("Assets") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddAsset,
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add asset"
                    )
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
                assetListState.assets.isEmpty() -> {
                    // Placeholder for no assets
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No assets to display", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = onNavigateToAddAsset) {
                            Text("Add your first asset")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(assetListState.assets) { asset ->
                            AssetListItem(
                                asset = asset.toPresentation(),
                                onRemove = { viewModel.handleIntent(AssetListIntent.RemoveAsset(asset.code)) }
                            )
                        }
                    }
                }
            }
        }
    }
}