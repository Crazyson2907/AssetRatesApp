package com.task.assetratesapp.presentation.addAssets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    onAssetSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    // Local state for search query.
    var searchQuery by remember { mutableStateOf("") }

    // A sample list of available asset codes.
    val availableAssets = listOf("USD", "EUR", "BTC", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK")

    // Filter the available assets based on the search query.
    val filteredAssets = if (searchQuery.isEmpty()) {
        availableAssets
    } else {
        availableAssets.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select an Asset") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            // Search field for filtering assets.
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Assets") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Display the filtered list.
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredAssets) { asset ->
                    // Each asset is rendered as a clickable ListItem.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onAssetSelected(asset) }
                    ) {
                        Text(
                            text = asset,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}